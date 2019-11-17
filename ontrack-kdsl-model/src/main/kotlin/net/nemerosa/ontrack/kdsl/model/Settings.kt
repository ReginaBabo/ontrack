package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.core.Ontrack
import net.nemerosa.ontrack.kdsl.core.Resource

/**
 * Access to the settings DSL
 */
class Settings : Resource()

/**
 * Extension point
 */
val Ontrack.settings: Settings
    get() = Settings().apply { init(this@settings) }

/**
 * Grant view to all
 */
var Settings.grantProjectViewToAll: Boolean
    get() {
        val settings = ontrackConnector.get("settings/general-security")
        return settings!!.get("grantProjectViewToAll").asBoolean()
    }
    set(value) {
        ontrackConnector.put(
                "settings/general-security",
                mapOf(
                        "grantProjectViewToAll" to value
                )
        )
    }