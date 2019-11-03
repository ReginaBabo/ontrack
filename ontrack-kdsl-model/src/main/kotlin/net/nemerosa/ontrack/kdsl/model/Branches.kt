package net.nemerosa.ontrack.kdsl.model

/**
 * Branch entity
 *
 * @property name Name of this branch
 * @property description Description of this branch
 * @property disabled State of this branch
 */
class Branch(
        id: Int,
        creation: Signature,
        val name: String,
        val description: String,
        val disabled: Boolean
) : ProjectEntityResource(id, creation)

/**
 * List of branches for a project.
 *
 * @param name Filter on the branch name (exact match)
 */
fun Project.branches(
        name: String? = null
): List<Branch> {
    TODO("List of branches for a project")
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
