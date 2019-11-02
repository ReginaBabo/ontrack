package net.nemerosa.ontrack.kdsl.client

import com.fasterxml.jackson.databind.JsonNode

interface OntrackConnector {

    fun download(path: String): ByteArray

    fun get(path: String): JsonNode?

    fun post(path: String, payload: Any?): JsonNode?

    fun put(path: String, payload: Any)

    fun delete(path: String)

}