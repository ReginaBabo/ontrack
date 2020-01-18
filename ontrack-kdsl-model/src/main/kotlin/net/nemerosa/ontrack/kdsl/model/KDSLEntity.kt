package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Entity
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.model.support.id

abstract class KDSLEntity(
        json: JsonNode,
        ontrackConnector: OntrackConnector
) : KDSLResource(json, ontrackConnector), Entity {

    override val id: Int = json.id

}