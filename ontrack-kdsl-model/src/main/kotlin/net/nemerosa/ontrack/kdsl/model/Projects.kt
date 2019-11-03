package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import net.nemerosa.ontrack.kdsl.core.Ontrack
import net.nemerosa.ontrack.kdsl.core.Resource

/**
 * Project entity
 *
 * @property name Name of this project
 * @property description Description of this project
 * @property disabled State of this project
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
 * @param name Filter on project name
 * @param favoritesOnly `true` if only the favorite projects must be returned
 * @param propertyType Filter on property type assigned to project
 * @param propertyValue Filter on [property type][propertyType] and property value assigned to project
 */
fun Ontrack.getProjects(
        name: String? = null,
        favoritesOnly: Boolean = false,
        propertyType: String? = null,
        propertyValue: String? = null
): List<Project> {
    val decl: String
    val params = mutableListOf<Resource.GraphQLParamImpl>()
    when {
        name != null -> {
            decl = "(name: ${'$'}name)"
            params += "name" type "String!" value name
        }
        propertyType != null -> {
            decl = """withProperty: {type: ${'$'}propertyType, value: ${'$'}propertyValue}"""
            params += "propertyType" type "String" value propertyType
            params += "propertyValue" type "String" value propertyValue
        }
        favoritesOnly -> {
            decl = "(favourites: true)"
        }
        else -> {
            decl = ""
        }
    }
    val query = """
        projects$decl {
            id
            name
            description
            disabled
            creation {
                user
                time
            }
        }
    """.trimIndent()
    // Query
    return query.graphQLQuery("Projects", *params.toTypedArray()).data["projects"].map {
        it.toConnector<Project>()
    }
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
fun Ontrack.findProjectByName(name: String): Project? =
        getProjects(name).firstOrNull()

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
        ).adaptSignature().toConnector()

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
 * Creates a project or returns it based on name.
 *
 * @param name Name of the project
 * @param description Description of the project
 * @param disabled State of the project
 * @return Project (created or retrieved)
 */
fun Ontrack.project(
        name: String,
        description: String = "",
        disabled: Boolean = false
): Project = project(name, description, disabled) { this }

/**
 * Updates this project.
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
    TODO("Updates this project.")
}

/**
 * Deletes this project.
 */
fun Project.delete() {
    TODO("Deletes this project")
}
