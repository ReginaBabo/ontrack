package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.PromotionRun
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.model.support.description

class KDSLPromotionRun(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLProjectEntity(json, ontrackConnector), PromotionRun {

    override val entityType: String = "PROMOTION_RUN"
    override val description: String = json.description

}