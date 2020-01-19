package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Build
import net.nemerosa.ontrack.dsl.PromotionRun
import net.nemerosa.ontrack.dsl.ValidationRun
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.description
import net.nemerosa.ontrack.kdsl.core.name

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
            postAndConvert(
                    link("promote"),
                    mapOf(
                            "promotionLevelName" to promotionLevel,
                            "description" to description
                    )
            ) { KDSLPromotionRun(it, ontrackConnector) }

    override val promotionRuns: List<PromotionRun>
        get() = getResources(link("promotionRuns")) { KDSLPromotionRun(it, ontrackConnector) }

    override fun validate(validationStamp: String, status: String?, description: String): ValidationRun =
            postAndConvert(
                    link("validate"),
                    mapOf(
                            "validationStampName" to validationStamp,
                            "validationRunStatusId" to status,
                            "description" to description
                    )
            ) { KDSLValidationRun(it, ontrackConnector) }
}