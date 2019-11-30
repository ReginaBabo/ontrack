package net.nemerosa.ontrack.graphql

import com.fasterxml.jackson.databind.node.TextNode
import net.nemerosa.ontrack.extension.general.validation.TextValidationDataType
import net.nemerosa.ontrack.model.structure.NameDescription
import net.nemerosa.ontrack.model.structure.ValidationRunStatusID
import net.nemerosa.ontrack.model.structure.config
import net.nemerosa.ontrack.model.structure.data
import net.nemerosa.ontrack.test.assertIs
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class ValidationRunGraphQLIT : AbstractQLKTITSupport() {

    @Autowired
    private lateinit var textValidationDataType: TextValidationDataType

    @Test
    fun `Text validation data type`() {
        val vs = doCreateValidationStamp(textValidationDataType.config(null))
        val build = doCreateBuild(vs.branch, NameDescription.nd("1", ""))
        val run = doValidateBuild(
                build,
                vs,
                ValidationRunStatusID.STATUS_PASSED,
                textValidationDataType.data("Some text")
        )
        // Checks the data
        assertEquals("Some text", run.data?.data)

        // Performs a query
        val data = asUser().withView(vs).call {
            run("""
                {
                    validationRuns(id: ${run.id}) {
                        data {
                            descriptor {
                                id
                                feature {
                                    id
                                }
                            }
                            data
                        }
                    }
                }
            """.trimIndent())
        }

        // Gets the data
        val runData = data["validationRuns"][0]["data"]["data"]
        assertIs<TextNode>(runData) {
            assertEquals("Some text", it.asText())
        }
    }

    @Test
    fun `Validation run statuses for a run for a validation stamp`() {
        // TODO Move to other test class
        project {
            branch {
                val vs = validationStamp()
                build("1") {
                    validate(vs, ValidationRunStatusID.STATUS_FAILED, description = "Validation failed").apply {
                        validationStatus(ValidationRunStatusID.STATUS_INVESTIGATING, "Investigating")
                        validationStatus(ValidationRunStatusID.STATUS_EXPLAINED, "Explained")
                    }
                    val data = run("""{
                        projects (id: ${project.id}) {
                            branches (name: "${branch.name}") {
                                validationStamps {
                                    name
                                    validationRuns {
                                        validationRunStatuses {
                                            statusID {
                                                id
                                            }
                                            description
                                        }
                                    }
                                }
                            }
                        }
                    }""")
                    val validationRunStatuses = data["projects"][0]["branches"][0]["validationStamps"][0]["validationRuns"][0]["validationRunStatuses"]
                    assertEquals(
                            listOf("EXPLAINED", "INVESTIGATING", "FAILED"),
                            validationRunStatuses.map { it["statusID"]["id"].asText() }
                    )
                    assertEquals(
                            listOf("Explained", "Investigating", "Validation failed"),
                            validationRunStatuses.map { it["description"].asText() }
                    )
                }
            }
        }
    }

}