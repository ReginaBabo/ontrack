package net.nemerosa.ontrack.kdsl.core

import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.client.OntrackConnectorProperties
import net.nemerosa.ontrack.kdsl.client.support.OntrackConnectorBuilder

/**
 * Root object of the Ontrack model. Typically extended by the model and
 * extensions for the Kotlin DSL.
 */
class Ontrack(ontrackConnector: OntrackConnector) : Resource() {

    init {
        this.ontrackConnector = ontrackConnector
        init()
    }

    companion object {
        /**
         * Utility method to get an [Ontrack] instance.
         */
        @JvmStatic
        fun connect(properties: OntrackConnectorProperties? = null): Ontrack {
            val ontrackConnector = OntrackConnectorBuilder.getOrCreateFromEnv(properties)
            return Ontrack(ontrackConnector)
        }
    }

    /**
     * Creates an anonymous connection
     */
    fun asAnonymous(): Ontrack = Ontrack(ontrackConnector.asAnonymous())
}
