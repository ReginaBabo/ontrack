package net.nemerosa.ontrack.kdsl.model

interface Build : ProjectEntity {
    val name: String
    val description: String

    /**
     * Previous build
     */
    val previousBuild: Build?

    /**
     * Next build
     */
    val nextBuild: Build?

    /**
     * Promotes a build.
     *
     * @param promotionLevel Name of the promotion level to promote to
     * @return Promotion run
     */
    fun promote(promotionLevel: String, description: String = ""): PromotionRun

    /**
     * Gets the list of promotion runs for this build
     */
    val promotionRuns: List<PromotionRun>

    /**
     * Validates a build.
     *
     * @receiver Build to validate
     * @param validationStamp Stamp to apply
     * @param status Validation status
     * @return Validation run
     */
    fun validate(
            validationStamp: String,
            status: String? = null,
            description: String = ""
    ): ValidationRun

    /**
     * Updates the build
     */
    fun update(name: String, description: String)

}
