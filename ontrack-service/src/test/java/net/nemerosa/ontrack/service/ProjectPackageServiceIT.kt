package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import net.nemerosa.ontrack.test.TestUtils.uid
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectPackageServiceIT : AbstractDSLTestSupport() {

    @Test
    fun `Packages for a project`() {
        val prefix = uid("G")
        project {
            // Setting some package ID
            packageIds {
                generic("$prefix.1")
            }
            // Looking another package ID
            asUserWithView(this).execute {
                val projects = projectPackageService.findProjectsWithPackageIdentifier(genericPackageId("$prefix.2"))
                assertTrue(projects.isEmpty())
            }
            // Sets another package ID
            packageIds {
                generic("$prefix.1")
                generic("$prefix.2")
            }
            // Looking for package ID
            asUserWithView(this).execute {
                val projects = projectPackageService.findProjectsWithPackageIdentifier(genericPackageId("$prefix.2"))
                assertEquals(
                        listOf(id),
                        projects.map { it.id }
                )
            }
            // Gets the package Ids
            val packages = asUserWithView(this).call {
                projectPackageService.getPackagesForProject(this)
            }
            assertEquals(
                    listOf(
                            "net.nemerosa.ontrack.service.GenericPackageType" to "$prefix.1",
                            "net.nemerosa.ontrack.service.GenericPackageType" to "$prefix.2"
                    ),
                    packages.map {
                        it.type.id to it.id
                    }
            )
        }
    }

}