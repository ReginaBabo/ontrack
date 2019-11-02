package net.nemerosa.ontrack.kdsl.model

/**
 * Resource with an ID
 *
 * @property id Unique ID in the scope of this entity type
 */
abstract class EntityResource(
        val id: Int
) : Resource()
