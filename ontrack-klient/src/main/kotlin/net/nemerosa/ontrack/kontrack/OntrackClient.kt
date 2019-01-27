package net.nemerosa.ontrack.kontrack

import com.fasterxml.jackson.databind.JsonNode

interface OntrackClient {

    fun get(url: String): JsonNode

}