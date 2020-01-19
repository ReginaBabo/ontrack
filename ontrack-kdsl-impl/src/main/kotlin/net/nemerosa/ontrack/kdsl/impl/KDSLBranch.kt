package net.nemerosa.ontrack.kdsl.impl

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.support.id
import net.nemerosa.ontrack.kdsl.core.support.name
import net.nemerosa.ontrack.kdsl.core.type
import net.nemerosa.ontrack.kdsl.core.value
import net.nemerosa.ontrack.kdsl.model.*

class KDSLBranch(
        json: JsonNode,
        ontrackConnector: OntrackConnector
) : KDSLProjectEntity(json, ontrackConnector), Branch {

    override val entityType: String = "BRANCH"

    override val name: String = json.name

    override val project: Project by lazy {
        ontrack.getProjectByID(json["project"].id)
    }

    override val promotionLevels: List<PromotionLevel>
        get() = getResources(link("promotionLevels")) {
            KDSLPromotionLevel(it, ontrackConnector)
        }

    override fun createPromotionLevel(name: String, description: String): PromotionLevel =
            postAndConvert(
                    link("createPromotionLevel"),
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            ) { KDSLPromotionLevel(it, ontrackConnector) }

    override fun validationStamps(name: String?): List<ValidationStamp> =
            graphQLQuery(
                    "ValidationStamps",
                    """
                        branches(id: ${'$'}id) {
                            validationStamps(name: ${'$'}name) {
                                json
                            }
                        }
                    """,
                    "id" type "Int!" value this.id,
                    "name" type "String" value name
            ).data["branches"][0]["validationStamps"].map {
                KDSLValidationStamp(it["json"], ontrackConnector)
            }

    override fun createValidationStamp(name: String, description: String): ValidationStamp =
            postAndConvert(
                    link("createValidationStamp"),
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            ) { KDSLValidationStamp(it, ontrackConnector) }

    override fun findBuildByName(name: String): Build? =
            ontrack.findBuildByName(
                    project.name,
                    this.name,
                    name
            )

    override fun createBuild(name: String, description: String): Build =
            postAndConvert(
                    link("createBuild"),
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            ) { KDSLBuild(it, ontrackConnector) }

    override fun filter(filterType: String, filterConfig: Map<String, Any>): List<Build> {
        val url = "structure/branches/$id/view/$filterType"
        val json = ontrackConnector.get(
                url,
                filterConfig
        )
        return json
                ?.get("buildViews")
                ?.map { buildView ->
                    KDSLBuild(buildView["build"], ontrackConnector)
                }
                ?: emptyList()
    }
}