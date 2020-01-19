package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.PromotionLevel
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.support.description
import net.nemerosa.ontrack.kdsl.core.support.name

class KDSLPromotionLevel(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLProjectEntity(json, ontrackConnector), PromotionLevel {

    override val entityType: String = "PROMOTION_LEVEL"
    override val name: String = json.name
    override val description: String = json.description

}