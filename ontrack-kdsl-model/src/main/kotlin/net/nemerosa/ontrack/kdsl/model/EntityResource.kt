package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.core.JsonResource

/**
 * Resource with an ID
 *
 * @property id Unique ID in the scope of this entity type
 */
abstract class EntityResource(
        json: JsonNode,
        val id: Int
) : JsonResource(json)
