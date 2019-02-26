package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import net.nemerosa.ontrack.model.security.BuildConfig
import net.nemerosa.ontrack.model.structure.*
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class BuildPackageVersionUploadServiceIT : AbstractDSLTestSupport() {

    @Autowired
    private lateinit var buildPackageVersionService: BuildPackageVersionService

    @Autowired
    private lateinit var buildPackageVersionUploadService: BuildPackageVersionUploadService

    @Test
    fun `Upload of packages`() {
        // Project and build acting as a reference
        val ref: Build = project<Build> {
            packageIds {
                test("net.nemerosa.ontrack:ontrack-model")
            }
            branch<Build> {
                build("3.38.5")
            }
        }
        // Source project
        project {
            branch {
                // Creates a build...
                val build = build()
                withGrantViewToAll {
                    // ... and uploads some package versions
                    build.uploadPackageVersions(
                            // Matching dependency
                            testPackageVersion("net.nemerosa.ontrack:ontrack-model", "3.38.5"),
                            // Unmatching dependency
                            testPackageVersion("org.apache.commons:commons-lang", "3.8.1")
                    )
                    asUserWithView(build).execute {
                        // Gets the package versions
                        val packageVersions = build.packageVersions
                        assertEquals(2, packageVersions.size)
                        // Matching
                        val matching = packageVersions[0]
                        assertEquals("net.nemerosa.ontrack:ontrack-model", matching.packageVersion.packageId.id)
                        assertEquals("3.38.5", matching.packageVersion.version)
                        assertEquals(ref.id(), matching.target?.id())
                        // Unmatching
                        val unmatching = packageVersions[1]
                        assertEquals("org.apache.commons:commons-lang", unmatching.packageVersion.packageId.id)
                        assertEquals("3.8.1", unmatching.packageVersion.version)
                        assertNull(unmatching.target)
                    }
                }
            }
        }
    }

    @Test
    fun `Upload of packages clears previous versions`() {
        project {
            branch {
                build {
                    // Uploads some versions
                    uploadPackageVersions(
                            testPackageVersion("net.nemerosa.ontrack:ontrack-model", "3.38.5"),
                            testPackageVersion("org.apache.commons:commons-lang", "3.8.1")
                    )
                    // Checks that only 2 versions are checked
                    asUserWithView(this).execute {
                        val versions = this.packageVersions
                        assertEquals(2, versions.size)
                    }
                    // Uploads some other versions
                    uploadPackageVersions(
                            testPackageVersion("net.nemerosa.ontrack:ontrack-model", "3.38.5")
                    )
                    // Checks that only 1 versions are checked
                    asUserWithView(this).execute {
                        val versions = this.packageVersions
                        assertEquals(1, versions.size, "Versions should have been cleared")
                    }
                }
            }
        }
    }

    @Test
    fun `Cannot upload if not authorized`() {
        project {
            branch {
                build {
                    assertFailsWith<AccessDeniedException> {
                        asUserWithView(this).execute {
                            buildPackageVersionUploadService.uploadAndResolvePackageVersions(
                                    this,
                                    arrayOf(testPackageVersion("net.nemerosa.ontrack:ontrack-model", "3.38.5"),
                                            testPackageVersion("org.apache.commons:commons-lang", "3.8.1")
                                    ).toList()
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `Upload needs only rights on parent build`() {
        // Project and build acting as a reference
        val ref: Build = project<Build> {
            packageIds {
                test("net.nemerosa.ontrack:ontrack-model")
            }
            branch<Build> {
                build("3.38.5")
            }
        }
        // Make sure to disable the "view all"
        withNoGrantViewToAll {
            // Source project
            project {
                branch {
                    // Creates a build...
                    val build = build()
                    // ... and uploads some package versions
                    build.uploadPackageVersions(
                            testPackageVersion("net.nemerosa.ontrack:ontrack-model", "3.38.5")
                    )
                    // Gets the package versions using a view role on ref project
                    asUserWithView(ref).withView(build).execute {
                        val packageVersions = build.packageVersions
                        assertEquals(1, packageVersions.size)
                        // Matching
                        val matching = packageVersions[0]
                        assertEquals("net.nemerosa.ontrack:ontrack-model", matching.packageVersion.packageId.id)
                        assertEquals("3.38.5", matching.packageVersion.version)
                        assertEquals(ref.id(), matching.target?.id(), "Ref build has been set")
                    }
                    // Gets the package versions not having a view role on ref project
                    asUserWithView(build).execute {
                        val packageVersions = build.packageVersions
                        assertEquals(1, packageVersions.size)
                        // Matching
                        val matching = packageVersions[0]
                        assertEquals("net.nemerosa.ontrack:ontrack-model", matching.packageVersion.packageId.id)
                        assertEquals("3.38.5", matching.packageVersion.version)
                        // Reference build is not visible
                        assertNull(matching.target, "Ref build not visible")
                    }
                }
            }
        }
    }

    private fun testPackageVersion(id: String, version: String): PackageVersion =
            testPackageId(id).toVersion(version)

    /**
     * Uploading some versions for a build
     */
    private fun Build.uploadPackageVersions(vararg packages: PackageVersion) {
        asUser().with(this, BuildConfig::class.java).execute {
            buildPackageVersionUploadService.uploadAndResolvePackageVersions(
                    this,
                    packages.toList()
            )
        }
    }

    /**
     * Gets the packages associated with a build
     */
    private val Build.packageVersions: List<BuildPackageVersion>
        get() = buildPackageVersionService.getBuildPackages(this)


}