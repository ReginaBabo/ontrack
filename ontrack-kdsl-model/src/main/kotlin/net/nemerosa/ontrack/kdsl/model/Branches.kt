package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.core.Ontrack
import net.nemerosa.ontrack.kdsl.core.id

/**
 * Branch entity
 *
 * @property name Name of this branch
 * @property description Description of this branch
 * @property disabled State of this branch
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Branch(
        json: JsonNode,
        id: Int,
        signature: Signature,
        val name: String,
        val description: String,
        val disabled: Boolean
) : ProjectEntityResource(json, id, signature) {

    override val entityType: String = "BRANCH"

    /**
     * Gets the project associated with this branch
     */
    val project: Project by lazy {
        ontrack.getProjectByID(json["project"].id)
    }

}

/**
 * List of branches for a project.
 */
fun Project.branches(): List<Branch> =
        ontrackConnector.get("structure/projects/$id/branches")
                ?.get("resources")
                ?.map { it.toConnector<Branch>() }
                ?: emptyList()

/**
 * List of branches for a project, filtered.
 *
 * @param name Filter on the branch name (regular expression)
 */
fun Project.branches(
        name: String
): List<Branch> =
        """
            branches(name: ${'$'}name, project: ${'$'}project) {
                json
            }
        """.trimIndent().graphQLQuery(
                "Branches",
                "name" type "String" value name,
                "project" type "String!" value this.name
        ).data["branches"].map {
            it["json"].toConnector<Branch>()
        }

/**
 * Creates a branch.
 *
 * @param name Name of the branch
 * @param description Description of the branch
 * @param disabled State of the branch
 * @return Created branch
 */
fun Project.createBranch(
        name: String,
        description: String = "",
        disabled: Boolean = false
): Branch =
        ontrackConnector.post(
                "structure/projects/$id/branches/create",
                mapOf(
                        "name" to name,
                        "description" to description,
                        "disabled" to disabled
                )
        ).toConnector()

/**
 * Creates or returns a branch and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the branch
 * @param description Description of the branch
 * @param disabled State of the branch
 * @param initFn Code to run against the created branch
 * @return Object return by [initFn]
 */
fun <T> Project.branch(
        name: String,
        description: String = "",
        disabled: Boolean = false,
        initFn: Branch.() -> T
): T {
    val b = branches(name = name).firstOrNull() ?: createBranch(name, description, disabled)
    return b.initFn()
}


/**
 * Creates or returns a branch
 *
 * @param name Name of the branch
 * @param description Description of the branch
 * @param disabled State of the branch
 * @return Created or retrieved branch
 */
fun Project.branch(
        name: String,
        description: String = "",
        disabled: Boolean = false
): Branch = branch(name, description, disabled) { this }

/**
 * Looking for a branch by name
 */
fun Ontrack.branch(project: String, branch: String): Branch? =
        ontrackConnector.get("structure/entity/branch/$project/$branch")?.toConnector()
