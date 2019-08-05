package net.nemerosa.ontrack.graphql

import net.nemerosa.ontrack.model.structure.ValidationRunStatusID
import net.nemerosa.ontrack.test.TestUtils
import org.junit.Test

class BuildSummaryGraphQLIT : AbstractQLKTITSupport() {

    // TODO With auto promotions
    // TODO With association of validations to promotions (without auto promotion)
    // TODO With count of comments

    @Test
    fun `List of build summaries with bare promotions`() {
        project {
            branch {
                // Validation stamps
                val built = validationStamp("built")
                val integration = validationStamp("integration")
                val acceptances = (1..5).map { validationStamp("acceptance-$it") }
                val publication = validationStamp("publication")
                // Promotion levels
                val iron = promotionLevel("IRON")
                val silver = promotionLevel("SILVER")
                val gold = promotionLevel("GOLD")
                val platinum = promotionLevel("PLATINUM")
                // Builds
                val build1 = build("1") {
                    validate(built)
                    validate(integration)
                    promote(iron)
                }
                val build2 = build("2") {
                    validate(built, ValidationRunStatusID.STATUS_FAILED)
                }
                val build3 = build("3") {
                    validate(built)
                    validate(integration, ValidationRunStatusID.STATUS_FAILED)
                }
                // Gets the build summary as GraphQL
                val data = run("""{
                    branches(id: $id) {
                        buildSummaries {
                            build {
                                name
                            }
                            promotions {
                                promotion {
                                    name
                                }
                                creation {
                                    user
                                }
                                validations {
                                    validationRunStatusId {
                                        name
                                    }
                                    count
                                }
                            }
                        }
                    }
                }""")
                // Gets the list of build summaries
                val buildSummaries = data["branches"]["buildSummaries"]
                // Checks the raw JSON
                TestUtils.assertJsonEquals(
                        TestUtils.resourceJson("/BuildSummaryGraphQLIT/barePromotions.json"),
                        buildSummaries
                )
            }
        }
    }

}