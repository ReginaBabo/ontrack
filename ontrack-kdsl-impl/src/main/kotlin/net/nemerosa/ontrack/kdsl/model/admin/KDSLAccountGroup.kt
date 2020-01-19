package net.nemerosa.ontrack.kdsl.model.admin

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.admin.AccountGroup
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.support.jsonText
import net.nemerosa.ontrack.kdsl.model.KDSLEntity

class KDSLAccountGroup(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLEntity(json, ontrackConnector), AccountGroup {

    override val name: String by jsonText(json)
    override val description: String by jsonText(json)

    override fun setGlobalPermission(role: String) {
        ontrackConnector.put(
                "accounts/permissions/globals/GROUP/$id",
                mapOf(
                        "role" to role
                )
        )
    }
}