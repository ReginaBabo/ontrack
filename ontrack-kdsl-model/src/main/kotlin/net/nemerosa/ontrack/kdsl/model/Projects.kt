package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.core.Ontrack

/**
 * Project entity
 *
 * @property name Name of this project
 * @property description Description of this project
 * @property disabled State of this project
 */
class Project(
        id: Int,
        creation: Signature,
        val name: String,
        val description: String,
        val disabled: Boolean
) : ProjectEntityResource(id, creation)

/**
 * Getting (filtered) list of projects.
 *
 * @param favoritesOnly `true` if only the favorite projects must be returned
 * @param propertyType Filter on property type assigned to project
 * @param propertyValue Filter on [property type][propertyType] and property value assigned to project
 */
fun Ontrack.getProjects(
        favoritesOnly: Boolean = false,
        propertyType: String? = null,
        propertyValue: String? = null
): List<Project> {
    TODO("Getting (filtered) list of projects")
}

/**
 * Gets a project by its ID.
 *
 * @param id ID of the project
 * @return Project
 * @throws EntityNotFoundException If no project can be found for this ID
 */
fun Ontrack.getProjectByID(id: Int): Project {
    TODO("Getting one project by ID")
}

/**
 * Gets a project by its name.
 *
 * @param name Exact name of the project
 * @return Project for this name, or `null` if not found.
 */
fun Ontrack.findProjectByName(name: String): Project? {
    TODO("Getting one project by name")
}

/**
 * Creates a project.
 *
 * @param name Name of the project
 * @param description Description of the project
 * @param disabled State of the project
 * @return Created project
 */
fun Ontrack.createProject(
        name: String,
        description: String = "",
        disabled: Boolean = false
): Project =
        ontrackConnector.post(
                "structure/projects/create",
                mapOf(
                        "name" to name,
                        "description" to description,
                        "disabled" to disabled
                )
        ).toConnector()

/**
 * Creates a project or returns it based on name, and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the project
 * @param description Description of the project
 * @param disabled State of the project
 * @param initFn Code to run against the created project
 * @return Object return by [initFn]
 */
fun <T> Ontrack.project(
        name: String,
        description: String = "",
        disabled: Boolean = false,
        initFn: Project.() -> T
): T {
    val p = findProjectByName(name) ?: createProject(name, description, disabled)
    return p.initFn()
}

/**
 * FIXME Updates this project.
 *
 * @param name New name (if not `null`)
 * @param description New description (if not `null`)
 * @param disabled New state (if not `null`)
 */
fun Project.update(
        name: String? = null,
        description: String? = null,
        disabled: Boolean? = null
) {
}

/**
 * FIXME Deletes this project.
 */
fun Project.delete() {
}