package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.core.Resource

/**
 * Resource with an ID
 *
 * @property id Unique ID in the scope of this entity type
 */
abstract class EntityResource(
        val id: Int
) : Resource()
