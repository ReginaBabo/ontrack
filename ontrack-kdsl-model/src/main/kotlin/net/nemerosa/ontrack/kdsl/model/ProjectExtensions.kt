package net.nemerosa.ontrack.kdsl.model


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
