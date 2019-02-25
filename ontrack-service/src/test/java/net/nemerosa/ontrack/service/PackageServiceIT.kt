package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PackageServiceIT : AbstractDSLTestSupport() {

    @Test
    fun toPackageId_null() {
        assertNull(packageService.toPackageId(null, true))
    }

    @Test
    fun toPackageId_empty() {
        assertNull(packageService.toPackageId("", true))
    }

    @Test
    fun toPackageId_blank() {
        assertNull(packageService.toPackageId(" ", true))
    }

    @Test
    fun toPackageId_one_token_no_error() {
        assertNull(packageService.toPackageId("type", false))
    }

    @Test
    fun toPackageId_one_token_with_error() {
        assertFailsWith<IllegalArgumentException> {
            assertNull(packageService.toPackageId("type", true))
        }
    }

    @Test
    fun toPackageId_three_tokens_no_error() {
        assertNull(packageService.toPackageId("type:id:one", false))
    }

    @Test
    fun toPackageId_three_tokens_with_error() {
        assertFailsWith<IllegalArgumentException> {
            assertNull(packageService.toPackageId("type:id:one", true))
        }
    }

    @Test
    fun toPackageId_unknown_type_no_error() {
        assertNull(packageService.toPackageId("type:id", false))
    }

    @Test
    fun toPackageId_unknown_type_with_error() {
        assertFailsWith<IllegalArgumentException> {
            assertNull(packageService.toPackageId("type:id", true))
        }
    }

    @Test
    fun toPackageId() {
        assertNotNull(packageService.toPackageId("net.nemerosa.ontrack.it.TestPackageType:id", true)) {
            assertEquals("net.nemerosa.ontrack.it.TestPackageType", it.type.id)
            assertEquals("Test", it.type.name)
            assertEquals("id", it.id)
        }
    }

    @Test
    fun toPackageVersion_null() {
        assertNull(packageService.toPackageVersion(null, true))
    }

    @Test
    fun toPackageVersion_empty() {
        assertNull(packageService.toPackageVersion("", true))
    }

    @Test
    fun toPackageVersion_blank() {
        assertNull(packageService.toPackageVersion(" ", true))
    }

    @Test
    fun toPackageVersion_one_token_no_error() {
        assertNull(packageService.toPackageVersion("type", false))
    }

    @Test
    fun toPackageVersion_one_token_with_error() {
        assertFailsWith<IllegalArgumentException> {
            assertNull(packageService.toPackageVersion("type", true))
        }
    }

    @Test
    fun toPackageVersion_two_token_no_error() {
        assertNull(packageService.toPackageVersion("type:id", false))
    }

    @Test
    fun toPackageVersion_two_token_with_error() {
        assertFailsWith<IllegalArgumentException> {
            assertNull(packageService.toPackageVersion("type:id", true))
        }
    }

    @Test
    fun toPackageVersion_four_tokens_no_error() {
        assertNull(packageService.toPackageVersion("type:id:one:two", false))
    }

    @Test
    fun toPackageVersion_four_tokens_with_error() {
        assertFailsWith<IllegalArgumentException> {
            assertNull(packageService.toPackageVersion("type:id:one:two", true))
        }
    }

    @Test
    fun toPackageVersion_unknown_type_no_error() {
        assertNull(packageService.toPackageVersion("type:id:one", false))
    }

    @Test
    fun toPackageVersion_unknown_type_with_error() {
        assertFailsWith<IllegalArgumentException> {
            assertNull(packageService.toPackageVersion("type:id:one", true))
        }
    }

    @Test
    fun toPackageVersion() {
        assertNotNull(packageService.toPackageVersion("net.nemerosa.ontrack.it.TestPackageType:id:one", true)) {
            assertEquals("net.nemerosa.ontrack.it.TestPackageType", it.packageId.type.id)
            assertEquals("Test", it.packageId.type.name)
            assertEquals("id", it.packageId.id)
            assertEquals("one", it.version)
        }
    }

}