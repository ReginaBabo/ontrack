package net.nemerosa.ontrack.dsl

interface Project : ProjectEntity {
    val name: String
    val description: String
    val branches: List<Branch>

    /**
     * List of branches for a project, filtered.
     *
     * @param name Filter on the branch name (regular expression)
     */
    fun branches(
            name: String
    ): List<Branch>

    /**
     * Creates a branch.
     *
     * @param name Name of the branch
     * @param description Description of the branch
     * @param disabled State of the branch
     * @return Created branch
     */
    fun createBranch(
            name: String,
            description: String = "",
            disabled: Boolean = false
    ): Branch

}