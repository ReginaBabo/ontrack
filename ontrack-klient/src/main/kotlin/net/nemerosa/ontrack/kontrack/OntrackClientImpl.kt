package net.nemerosa.ontrack.kontrack

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.client.JsonClient
import net.nemerosa.ontrack.client.JsonClientImpl
import net.nemerosa.ontrack.client.OTHttpClient

/**
 * Low level client
 *
 * @property httpClient HTTP layer
 */
class OntrackClientImpl(
        val httpClient: OTHttpClient
) {

    private val jsonClient: JsonClient = JsonClientImpl(httpClient)

    fun get(url: String): JsonNode {
        return jsonClient.get(url)
    }

}
