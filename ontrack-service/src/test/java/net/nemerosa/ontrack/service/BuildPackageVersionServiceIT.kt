package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.BuildPackageVersion
import net.nemerosa.ontrack.model.structure.BuildPackageVersionService
import net.nemerosa.ontrack.model.structure.PackageId
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

    @Test
    fun onUnassignedPackages() {
        val id1 = testPackageId("id1")
        val id2 = testPackageId("id2")
        project project@{
            branch {
                val ref1 = build {}
                val ref2 = build {}
                // Two versions, one with reference
                val parent1 = build {
                    packageVersion(id1, "1.0.0", null)
                    packageVersion(id2, "2.0.0", ref2)
                }
                // Two versions, two with reference
                build {
                    packageVersion(id1, "1.0.0", ref1)
                    packageVersion(id2, "2.0.0", ref2)
                }
                // Two versions, none with reference
                val parent0 = build {
                    packageVersion(id1, "1.0.0", null)
                    packageVersion(id2, "2.0.0", null)
                }

                // Loops on the list of unassigned packages
                val result = mutableSetOf<String>()
                buildPackageVersionService.onUnassignedPackages(this@project) { build, p ->
                    result.add(
                            "${build.id()}::${p.packageVersion.packageId.id}::${p.packageVersion.version}::${p.target?.id()
                                    ?: "-"}"
                    )
                }

                assertEquals(
                        setOf(
                                "${parent1.id()}::id1::1.0.0::-",
                                "${parent0.id()}::id1::1.0.0::-",
                                "${parent0.id()}::id2::2.0.0::-"
                        ),
                        result
                )

            }
        }
    }

    private fun Build.packageVersion(id: PackageId, version: String, ref: Build?) {
        buildPackageVersionService.saveBuildPackage(
                this,
                BuildPackageVersion(id.toVersion(version), ref)
        )
    }

}