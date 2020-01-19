package net.nemerosa.ontrack.dsl

/**
 * Base exception
 */
abstract class DSLException(message: String) : RuntimeException(message)

/**
 * Thrown when an entity cannot be found using its ID.
 */
class EntityNotFoundException(name: String, id: Int) : DSLException(
        "No $name with $id can be found"
)

/**
 * Thrown when a branch cannot be found by name
 */
class BranchNotFoundException(project: String, branch: String) : DSLException(
        "Branch $project/$branch not found"
)

/**
 * Thrown when a build cannot be found by name
 */
class BuildNotFoundException(project: String, branch: String, build: String) : DSLException(
        "Build $project/$branch/$build not found"
)
