package net.nemerosa.ontrack.kdsl.model.admin

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.admin.Account
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.support.jsonText
import net.nemerosa.ontrack.kdsl.model.KDSLEntity

class KDSLAccount(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLEntity(json, ontrackConnector), Account {
    override val name: String by jsonText(json)
    override val fullName: String by jsonText(json)
    override val email: String by jsonText(json)
}