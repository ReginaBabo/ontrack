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


    /**
     * Searching a build on a project
     */
    fun searchBuilds(
            maximumCount: Int = 10,
            branchName: String? = null,
            buildName: String? = null,
            buildExactMatch: Boolean = false,
            promotionName: String? = null,
            validationStampName: String? = null,
            property: String? = null,
            propertyValue: String? = null,
            linkedFrom: String? = null,
            linkedTo: String? = null
    ): List<Build>

}