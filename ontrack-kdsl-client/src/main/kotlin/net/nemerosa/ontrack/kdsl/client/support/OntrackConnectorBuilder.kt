package net.nemerosa.ontrack.kdsl.client.support

import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.client.OntrackConnectorProperties
import net.nemerosa.ontrack.kdsl.client.impl.HttpOntrackConnector

object OntrackConnectorBuilder {

    var ontrackConnector: OntrackConnector? = null

    fun getOrCreateFromEnv(ontrackConnectorProperties: OntrackConnectorProperties): OntrackConnector =
            ontrackConnector ?: createFromEnv(ontrackConnectorProperties).apply { ontrackConnector = this }

    private fun createFromEnv(ontrackConnectorProperties: OntrackConnectorProperties): OntrackConnector {
        return HttpOntrackConnector(
                url = ontrackConnectorProperties.uri,
                username = ontrackConnectorProperties.username,
                password = ontrackConnectorProperties.password
        )
    }
}