package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Build
import net.nemerosa.ontrack.dsl.PromotionRun
import net.nemerosa.ontrack.dsl.ValidationRun
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.model.support.description
import net.nemerosa.ontrack.kdsl.model.support.name
import net.nemerosa.ontrack.kdsl.model.support.resources

class KDSLBuild(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLProjectEntity(json, ontrackConnector), Build {

    override val entityType: String = "BUILD"
    override val name: String = json.name
    override val description: String = json.description

    override fun update(name: String, description: String) {
        ontrackConnector.put(
                link("update"),
                mapOf(
                        "name" to name,
                        "description" to description
                )
        )
    }

    override val previousBuild: Build? by lazy {
        ontrackConnector.get(link("previous"))?.let { KDSLBuild(it, ontrackConnector) }
    }

    override val nextBuild: Build?
        // Cannot be stored because can change
        get() = ontrackConnector.get(link("next"))?.let { KDSLBuild(it, ontrackConnector) }

    override fun promote(promotionLevel: String, description: String): PromotionRun =
            ontrackConnector.post(
                    link("promote"),
                    mapOf(
                            "promotionLevelName" to promotionLevel,
                            "description" to description
                    )
            )?.let { KDSLPromotionRun(it, ontrackConnector) } ?: throw MissingResponseException()

    override val promotionRuns: List<PromotionRun>
        get() = ontrackConnector.get(link("promotionRuns"))
                ?.resources
                ?.map { KDSLPromotionRun(it, ontrackConnector) }
                ?: emptyList()

    override fun validate(validationStamp: String, status: String?, description: String): ValidationRun =
            ontrackConnector.post(
                    link("validate"),
                    mapOf(
                            "validationStampName" to validationStamp,
                            "validationRunStatusId" to status,
                            "description" to description
                    )
            )?.let { KDSLValidationRun(it, ontrackConnector) } ?: throw MissingResponseException()
}