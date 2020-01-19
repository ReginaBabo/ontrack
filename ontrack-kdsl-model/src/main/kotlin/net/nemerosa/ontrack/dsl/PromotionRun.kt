package net.nemerosa.ontrack.dsl

interface PromotionRun : ProjectEntity {

    val description: String

    /**
     * Associated promotion level
     */
    val promotionLevel: PromotionLevel

    /**
     * Deletes this promotion run
     */
    fun delete()

}
