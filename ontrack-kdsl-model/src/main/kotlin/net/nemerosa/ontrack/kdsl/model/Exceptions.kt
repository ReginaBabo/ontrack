package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.core.support.DSLException

/**
 * Thrown when an entity cannot be found using its ID.
 */
class EntityNotFoundException(name: String, id: Int) : DSLException(
        "No $name with $id can be found"
)

/**
 * Thrown when a build cannot be found by name
 */
class BuildNotFoundException(project: String, branch: String, build: String) : DSLException(
        "Build $project/$branch/$build not found"
)
