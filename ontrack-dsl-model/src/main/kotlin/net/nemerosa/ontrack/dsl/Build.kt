package net.nemerosa.ontrack.dsl

interface Build : ProjectEntity {
    val name: String
    val description: String

    /**
     * Promotes a build.
     *
     * @param promotionLevel Name of the promotion level to promote to
     * @return Promotion run
     */
    fun promote(promotionLevel: String, description: String = ""): PromotionRun

}
