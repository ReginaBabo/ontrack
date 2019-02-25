package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import net.nemerosa.ontrack.model.structure.BuildPackageVersion
import net.nemerosa.ontrack.model.structure.BuildPackageVersionService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class BuildPackageVersionServiceIT : AbstractDSLTestSupport() {

    @Autowired
    private lateinit var buildPackageVersionService: BuildPackageVersionService

    @Test
    fun saveBuildPackage() {
        project {
            val id = testPackageId("id")
            branch {
                val buildRef = build()
                build {
                    buildPackageVersionService.saveBuildPackage(
                            this,
                            BuildPackageVersion(
                                    id.toVersion("1.0.0"),
                                    null // No target
                            )
                    )
                    // Gets list of packages for this build
                    val packages = buildPackageVersionService.getBuildPackages(this)
                    assertEquals(1, packages.size)
                    // Saves it again, with a target
                    buildPackageVersionService.saveBuildPackage(
                            this,
                            BuildPackageVersion(
                                    id.toVersion("1.0.0"),
                                    buildRef
                            )
                    )
                    // Gets list of packages for this build
                    val newPackages = buildPackageVersionService.getBuildPackages(this)
                    assertEquals(1, newPackages.size)
                }
            }
        }
    }

}