package net.nemerosa.ontrack.graphql

import net.nemerosa.ontrack.model.structure.Build
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BuildPackageVersionsGraphQLIT : AbstractQLKTITSupport() {

    @Test
    fun `Build package versions unfiltered`() {
        // Project to use as reference
        val ref = project<Build> {
            packageIds {
                test("net.nemerosa.ontrack:ontrack-model")
                test("nemerosa/ontrack")
            }
            branch<Build> {
                build("3.38.5")
            }
        }

        // Parent project & build
        project {
            branch {
                build {
                    // ... and uploads some package versions
                    uploadPackageVersions(
                            // Matching dependency
                            testPackageVersion("net.nemerosa.ontrack:ontrack-model", "3.38.5"),
                            // Unmatching dependency
                            testPackageVersion("org.apache.commons:commons-lang", "3.8.1")
                    )
                }
                // Gets its versions using GraphQL
                val data = run("""{
                    builds(id: $id) {
                        packageVersions {
                            packageId {
                                type {
                                    name
                                }
                                id
                            }
                            version
                            target {
                                id
                            }
                        }
                    }
               }""")

                val versions = data["builds"][0]["packageVersions"]
                assertEquals(2, versions.size())

                val ontrackVersion = versions[0]
                assertEquals("Test", ontrackVersion["packageId"]["type"]["name"].asText())
                assertEquals("net.nemerosa.ontrack:ontrack-model", ontrackVersion["packageId"]["id"].asText())
                assertEquals("3.38.5", ontrackVersion["version"].asText())
                assertNotNull(ontrackVersion["target"]) {
                    assertEquals(ref.id(), it["id"].asInt())
                }

                val commonsVersion = versions[1]
                assertEquals("Test", commonsVersion["packageId"]["type"]["name"].asText())
                assertEquals("org.apache.commons:commons-lang", commonsVersion["packageId"]["id"].asText())
                assertEquals("3.8.1", commonsVersion["version"].asText())
                assertNull(commonsVersion["target"])
            }
        }
    }

    @Test
    fun `Build package versions filtered`() {
        // Project to use as reference
        val ref = project<Build> {
            packageIds {
                test("net.nemerosa.ontrack:ontrack-model")
                test("nemerosa/ontrack")
            }
            branch<Build> {
                build("3.38.5")
            }
        }

        // Parent project & build
        project {
            branch {
                build {
                    // ... and uploads some package versions
                    uploadPackageVersions(
                            // Matching dependency
                            testPackageVersion("net.nemerosa.ontrack:ontrack-model", "3.38.5"),
                            // Unmatching dependency
                            testPackageVersion("org.apache.commons:commons-lang", "3.8.1")
                    )
                }
                // Gets its versions using GraphQL
                val data = run("""{
                    builds(id: $id) {
                        packageVersions(linkedOnly: true) {
                            packageId {
                                type {
                                    name
                                }
                                id
                            }
                            version
                            target {
                                id
                            }
                        }
                    }
               }""")

                val versions = data["builds"][0]["packageVersions"]
                assertEquals(1, versions.size())

                val ontrackVersion = versions[0]
                assertEquals("Test", ontrackVersion["packageId"]["type"]["name"].asText())
                assertEquals("net.nemerosa.ontrack:ontrack-model", ontrackVersion["packageId"]["id"].asText())
                assertEquals("3.38.5", ontrackVersion["version"].asText())
                assertNotNull(ontrackVersion["target"]) {
                    assertEquals(ref.id(), it["id"].asInt())
                }
            }
        }
    }

}