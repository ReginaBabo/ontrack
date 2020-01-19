package net.nemerosa.ontrack.kdsl.model.admin

import net.nemerosa.ontrack.dsl.admin.Settings
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.Connector
import net.nemerosa.ontrack.kdsl.core.MissingResponseException

class KDSLSettings(ontrackConnector: OntrackConnector) : Connector(ontrackConnector), Settings {

    override var grantProjectViewToAll: Boolean
        get() = ontrackConnector.get("settings/general-security")
                ?.get("grantProjectViewToAll")?.booleanValue()
                ?: throw MissingResponseException()
        set(value) {
            ontrackConnector.put(
                    "settings/general-security",
                    mapOf(
                            "grantProjectViewToAll" to value
                    )
            )
        }
}
