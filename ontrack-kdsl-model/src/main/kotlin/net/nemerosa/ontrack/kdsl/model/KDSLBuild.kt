package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.PromotionRun
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.model.support.description
import net.nemerosa.ontrack.kdsl.model.support.name

class KDSLBuild(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLProjectEntity(json, ontrackConnector), net.nemerosa.ontrack.dsl.Build {

    override val entityType: String = "BUILD"
    override val name: String = json.name
    override val description: String = json.description

    override fun promote(promotionLevel: String, description: String): PromotionRun =
            ontrackConnector.post(
                    "structure/builds/$id/promotionRun/create",
                    mapOf(
                            "promotionLevelName" to promotionLevel,
                            "description" to description
                    )
            )?.let { KDSLPromotionRun(it, ontrackConnector) } ?: throw MissingResponseException()


}