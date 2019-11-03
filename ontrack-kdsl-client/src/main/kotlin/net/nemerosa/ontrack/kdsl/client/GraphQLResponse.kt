package net.nemerosa.ontrack.kdsl.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode

@JsonIgnoreProperties(ignoreUnknown = true)
class GraphQLResponse(
        val data: JsonNode
)
