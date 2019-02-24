package net.nemerosa.ontrack.graphql

import net.nemerosa.ontrack.json.toJson
import net.nemerosa.ontrack.test.TestUtils
import net.nemerosa.ontrack.test.TestUtils.uid
import org.junit.Test
import kotlin.test.assertEquals

class ProjectPackageGraphQLIT : AbstractQLKTITSupport() {

    @Test
    fun `Project package ids`() {
        project {
            packageIds {
                test("one")
                test("two")
            }
            val data = run("""{
                projects(id: ${this.id}) {
                    packageIds {
                        type {
                            id
                            name
                        }
                        id
                    }
                }
            }""")
            val project = data["projects"][0]
            val packages = project["packageIds"]
            TestUtils.assertJsonEquals(
                    listOf(
                            mapOf(
                                    "type" to mapOf(
                                            "id" to "net.nemerosa.ontrack.it.TestPackageType",
                                            "name" to "Test"
                                    ),
                                    "id" to "one"
                            ),
                            mapOf(
                                    "type" to mapOf(
                                            "id" to "net.nemerosa.ontrack.it.TestPackageType",
                                            "name" to "Test"
                                    ),
                                    "id" to "two"
                            )
                    ).toJson(),
                    packages
            )
        }
    }

    @Test
    fun `List of projects for a package ID`() {
        val prefix = uid("P")
        val p1 = project {
            packageIds {
                test("$prefix-one")
            }
        }
        project {
            packageIds {
                test("$prefix-two")
            }
        }
        val data = run("""{
                projects(packageId: "net.nemerosa.ontrack.it.TestPackageType:$prefix-one") {
                    id
                }
            }""")
        val projects = data["projects"]
        assertEquals(1, projects.size())
        val project = projects[0]
        assertEquals(p1.id(), project["id"].asInt())
    }

}