package net.nemerosa.ontrack.kdsl.impl

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.PromotionLevel
import net.nemerosa.ontrack.dsl.PromotionRun
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.support.description
import net.nemerosa.ontrack.kdsl.core.support.id

class KDSLPromotionRun(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLProjectEntity(json, ontrackConnector), PromotionRun {

    override val entityType: String = "PROMOTION_RUN"
    override val description: String = json.description

    override val promotionLevel: PromotionLevel by lazy {
        ontrack.getPromotionLevelByID(json["promotionLevel"].id)
    }

    override fun delete() {
        ontrackConnector.delete(link("delete"))
    }
}