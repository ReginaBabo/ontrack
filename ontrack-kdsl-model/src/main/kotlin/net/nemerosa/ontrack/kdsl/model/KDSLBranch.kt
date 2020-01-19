package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.*
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.type
import net.nemerosa.ontrack.kdsl.core.value
import net.nemerosa.ontrack.kdsl.model.support.id
import net.nemerosa.ontrack.kdsl.model.support.name
import net.nemerosa.ontrack.kdsl.model.support.resources

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
        get() = ontrackConnector.get(link("promotionLevels"))
                ?.resources
                ?.map { KDSLPromotionLevel(it, ontrackConnector) } ?: throw MissingResponseException()

    override fun createPromotionLevel(name: String, description: String): PromotionLevel =
            ontrackConnector.post(
                    link("createPromotionLevel"),
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            )?.let { KDSLPromotionLevel(it, ontrackConnector) } ?: throw MissingResponseException()

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
            ontrackConnector.post(
                    link("createValidationStamp"),
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            )?.let { KDSLValidationStamp(it, ontrackConnector) } ?: throw MissingResponseException()

    override fun findBuildByName(name: String): Build? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createBuild(name: String, description: String): Build =
            ontrackConnector.post(
                    link("createBuild"),
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            )?.let { KDSLBuild(it, ontrackConnector) } ?: throw MissingResponseException()

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