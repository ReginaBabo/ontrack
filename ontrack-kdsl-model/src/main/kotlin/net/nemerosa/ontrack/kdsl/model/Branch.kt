package net.nemerosa.ontrack.kdsl.model

interface Branch : ProjectEntity {
    val name: String
    val project: Project

    /**
     * List of promotion levels for a branch.
     */
    val promotionLevels: List<PromotionLevel>

    /**
     * Creates a promotion level.
     *
     * @param name Name of the promotion level
     * @param description Description of the promotion level
     * @return Created promotion level
     */
    fun createPromotionLevel(
            name: String,
            description: String = ""
    ): PromotionLevel

    /**
     * List of validation stamps for a branch.
     *
     * @param name Filter on the validation stamp name (exact match)
     */
    fun validationStamps(
            name: String? = null
    ): List<ValidationStamp>

    /**
     * Creates a validation stamp.
     *
     * @param name Name of the validation stamp
     * @param description Description of the validation stamp
     * @return Created validation stamp
     */
    fun createValidationStamp(
            name: String,
            description: String = ""
    ): ValidationStamp

    /**
     * Finds a build by name in this branch
     *
     * @param name Name of the build to find
     * @return Build or `null` if not found
     */
    fun findBuildByName(name: String): Build?

    /**
     * Creates a build for the branch
     *
     * @receiver Branch where to create the build
     * @param name Build name
     * @param description Build description
     * @return Created build
     */
    fun createBuild(name: String, description: String): Build

    /**
     * Runs any filter and returns the list of corresponding builds.
     */
    fun filter(filterType: String, filterConfig: Map<String, Any>): List<Build>

}
