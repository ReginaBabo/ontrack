package net.nemerosa.ontrack.kdsl.model

/**
 * Thrown when an entity cannot be found using its ID.
 */
class EntityNotFoundException(name: String, id: Int) : RuntimeException(
        "No $name with $id can be found"
)
