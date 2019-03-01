package net.nemerosa.ontrack.service

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import net.nemerosa.ontrack.common.Document
import net.nemerosa.ontrack.extension.api.support.TestExtensionFeature
import net.nemerosa.ontrack.extension.support.AbstractExtension
import net.nemerosa.ontrack.it.TestPackageType
import net.nemerosa.ontrack.model.exceptions.BuildPackageVersionUploadParsingException
import net.nemerosa.ontrack.model.exceptions.BuildPackageVersionUploadWrongMimeTypeException
import net.nemerosa.ontrack.model.structure.BuildPackageVersionUploadParsingService
import net.nemerosa.ontrack.model.structure.PackageService
import net.nemerosa.ontrack.model.structure.PackageType
import net.nemerosa.ontrack.model.structure.PackageVersion
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BuildPackageVersionUploadParsingServiceImplTest {

    private lateinit var service: BuildPackageVersionUploadParsingService
    private lateinit var packageService: PackageService

    private val testFeature = TestExtensionFeature()
    private val defaultPackageType = TestPackageType(testFeature)

    @Before
    fun init() {
        packageService = mock()
        service = BuildPackageVersionUploadParsingServiceImpl(listOf(
                TomlBuildPackageVersionParser(packageService),
                PropertiesBuildPackageVersionParser(packageService)
        ))

        whenever(packageService.findByNameOrId("test")).thenReturn(defaultPackageType)
        whenever(packageService.findByNameOrId("maven")).thenReturn(MockPackageType("Maven"))
        whenever(packageService.findByNameOrId("npm")).thenReturn(MockPackageType("Npm"))
    }

    @Test
    fun `Empty document`() {
        val versions = service.parsePackageVersions(defaultPackageType, Document.EMPTY)
        assertTrue(versions.isEmpty(), "No version")
    }

    @Test
    fun `Empty text`() {
        val versions = parse(mime = "application/toml", text = "")
        assertTrue(versions.isEmpty(), "No version")
    }

    @Test
    fun `Wrong MIME type`() {
        assertFailsWith<BuildPackageVersionUploadWrongMimeTypeException> {
            service.parsePackageVersions(defaultPackageType, Document("application/json", "some content".toByteArray()))
        }
    }

    @Test
    fun `Toml parsing`() {
        val versions = parse(mime = "application/toml", text = """
            'id.one' = 1.0
            'id.two' = 2.0

            [test]
            'id.three' = 2.1

            [maven]
            'group1:artifact1' = 3.0
            'group2:artifact2' = 4.0

            [npm]
            package-1 = 5.0
            package-2 = 6.0
        """)
        versions.assertVersionsPresent(
                "test::id.one::1.0",
                "test::id.two::2.0",
                "test::id.three::2.1",
                "maven::group1:artifact1::3.0",
                "maven::group2:artifact2::4.0",
                "npm::package-1::5.0",
                "npm::package-2::6.0"
        )
    }

    @Test
    fun `Not existing type in Toml`() {
        assertFailsWith<BuildPackageVersionUploadParsingException> {
            parse(mime = "application/toml", text = """
                [not-found]
                some-id = some-version
            """.trimIndent())
        }
    }

    @Test
    fun `Properties parsing`() {
        val versions = parse(mime = "text/plain", text = """
            id.one = 1.0
            id.two = 2.0

            # Explicit type
            test::id.three = 2.1

            # Maven dependencies
            maven::group1:artifact1 = 3.0
            maven::group2:artifact2 = 4.0

            # Npm dependencies
            npm::package-1 = 5.0
            npm::package-2 = 6.0
        """)
        versions.assertVersionsPresent(
                "test::id.one::1.0",
                "test::id.two::2.0",
                "test::id.three::2.1",
                "maven::group1:artifact1::3.0",
                "maven::group2:artifact2::4.0",
                "npm::package-1::5.0",
                "npm::package-2::6.0"
        )
    }

    private fun List<PackageVersion>.assertVersionsPresent(vararg versions: String) {
        versions.forEach { version -> assertVersionPresent(version) }
    }

    private fun List<PackageVersion>.assertVersionPresent(version: String) {
        val versions = map {
            "${it.packageId.type.name.toLowerCase()}::${it.packageId.id}::${it.version}"
        }.toSet()
        assertTrue(
                versions.contains(version),
                "$version has not been found"
        )
    }

    private fun parse(defaultType: PackageType = defaultPackageType, mime: String, text: String) =
            service.parsePackageVersions(defaultType, document(mime, text))

    private fun document(mime: String, text: String): Document = Document(
            mime,
            text.toByteArray()
    )

    private inner class MockPackageType(override val name: String) : AbstractExtension(testFeature), PackageType {
        override val description: String
            get() = "Type $name"
    }

}