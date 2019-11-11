package net.nemerosa.ontrack.kdsl.core

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * Object which gets an internal link to Ontrack through an [OntrackConnector].
 */
abstract class Connector {

    /**
     * Internal link to Ontrack
     */
    lateinit var ontrackConnector: OntrackConnector

    /**
     * Initialisation of the link
     */
    fun init(connector: Connector) {
        ontrackConnector = connector.ontrackConnector
        init()
    }

    /**
     * Function to call to initialise the rest of the object
     */
    open fun init() {}

    /**
     * Access to the Ontrack root
     */
    val ontrack: Ontrack get() = Ontrack(ontrackConnector)

    inline fun <reified T> JsonNode?.toObject(): T =
            this?.parse<T>() ?: throw IllegalStateException("Cannot convert a null JSON object")

    inline fun <reified T : Resource> JsonNode?.toConnector(): T {
        val t = toObject<T>()
        t.ontrackConnector = this@Connector.ontrackConnector
        t.init()
        return t
    }

}
