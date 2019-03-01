package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.common.Document
import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import net.nemerosa.ontrack.model.structure.BuildPackageVersionUploadParsingService
import net.nemerosa.ontrack.model.structure.PackageVersion
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertTrue

class GeneralPackageTypeIT : AbstractDSLTestSupport() {

    @Autowired
    private lateinit var parsingService: BuildPackageVersionUploadParsingService

    @Autowired
    private lateinit var mavenPackageType: MavenPackageType

    @Test
    fun `Parsing of package versions using Toml`() {
        val versions = parsingService.parsePackageVersions(mavenPackageType, Document(
                "application/toml",
                """
                    'group1:artifact1' = 1.0

                    [maven]
                    'group2:artifact2' = 2.0

                    [docker]
                    'nemerosa/ontrack' = "3.38.5"

                    [npm]
                    my-package = 3.0

                    [generic]
                    id = 4.0
                """.toByteArray()
        ))

        checks(versions)
    }

    @Test
    fun `Parsing of package versions using properties`() {
        val versions = parsingService.parsePackageVersions(mavenPackageType, Document(
                "application/properties",
                """
                    group1:artifact1 = 1.0

                    maven::group2:artifact2 = 2.0

                    docker::nemerosa/ontrack = 3.38.5

                    npm::my-package = 3.0

                    generic::id = 4.0
                """.toByteArray()
        ))

        checks(versions)
    }

    private fun checks(versions: List<PackageVersion>) {
        assertVersion(versions, "Maven", "group1:artifact1", "1.0")
        assertVersion(versions, "Maven", "group2:artifact2", "2.0")
        assertVersion(versions, "Docker", "nemerosa/ontrack", "3.38.5")
        assertVersion(versions, "Npm", "my-package", "3.0")
        assertVersion(versions, "Generic", "id", "4.0")
    }

    private fun assertVersion(versions: List<PackageVersion>, type: String, id: String, version: String) {
        assertTrue(
                versions.any {
                    it.packageId.type.name == type &&
                            it.packageId.id == id &&
                            it.version == version
                },
                "Could not find version $type:$id:$version"
        )
    }

}