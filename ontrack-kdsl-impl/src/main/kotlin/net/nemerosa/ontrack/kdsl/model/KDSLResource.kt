package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Ontrack
import net.nemerosa.ontrack.dsl.Resource
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.Connector

abstract class KDSLResource(
        protected val json: JsonNode,
        ontrackConnector: OntrackConnector
) : Connector(ontrackConnector), Resource {

    override val ontrack: Ontrack by lazy {
        KDSLOntrack(ontrackConnector)
    }

    protected fun link(name: String): String = optionalLink(name) ?: throw ResourceMissingLinkException(name)

    protected fun optionalLink(name: String): String? {
        val linkName = if (name.startsWith('_')) name else "_$name"
        return if (json.has(linkName)) {
            json[linkName].textValue().removePrefix("/")
        } else {
            null
        }
    }

}
