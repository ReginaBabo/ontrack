package net.nemerosa.ontrack.graphql

import org.junit.Test
import kotlin.test.assertEquals

class BuildFilterGraphQLIT : AbstractGraphQLITSupport() {

    @Test
    fun `Last promoted build`() {
        project {
            branch {
                // Creating a promotion level
                val pl = promotionLevel()
                // Creating a first promoted build
                val build1 = build("1") {
                    promote(pl)
                }
                // Creating a second promoted build
                val build2 = build("2") {
                    promote(pl)
                }
                // Run a GraphQL query at project level and gets the last promotion run
                val data = run("""{
                    |   projects(id: ${pl.project.id}) {
                    |      branches {
                    |          promotionLevels {
                    |              name
                    |              promotionRuns(first: 1) {
                    |                build {
                    |                  name
                    |                }
                    |              }
                    |          }
                    |      }
                    |   }
                    |}
                """.trimMargin())
                // Checks that the build associated with the promotion is the last one
                val plNode = data["projects"][0]["branches"][0]["promotionLevels"][0]
                assertEquals(pl.name, plNode["name"].asText())
                val runNodes = plNode["promotionRuns"]
                assertEquals(1, runNodes.size())
                val build = runNodes[0]["build"]
                assertEquals(build2.name, build["name"].asText())
            }
        }
    }

    @Test
    fun `Last promotion run only`() {
        project {
            branch {
                val pl = promotionLevel("COPPER")
                // Build promoted twice
                build {
                    promote(pl, "Once")
                    promote(pl, "Twice")
                }
                // Asks for the promotion runs of the build
                val data = run("""{
                   branches(id: $id) {
                      builds {
                          promotionRuns(lastPerLevel: true) {
                              promotionLevel {
                                  name
                              }
                              description
                          }
                      }
                   }
                }""".trimMargin())
                // Gets the first build
                val buildNode = data.path("branches").get(0).path("builds").get(0)
                // Gets the promotion runs
                val promotionRuns = buildNode.path("promotionRuns")
                assertEquals(1, promotionRuns.size())
                val promotionRun = promotionRuns.get(0)
                assertEquals("Twice", promotionRun.path("description").asText())
                assertEquals("COPPER", promotionRun.path("promotionLevel").path("name").asText())
            }
        }
    }
}