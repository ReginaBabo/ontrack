package net.nemerosa.ontrack.kdsl.core

import com.fasterxml.jackson.databind.JsonNode

/**
 * [Resource] based on a JSON representation.
 */
abstract class JsonResource(
        protected val json: JsonNode
) : Resource()
