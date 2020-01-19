package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.model.ProjectEntity
import net.nemerosa.ontrack.kdsl.model.PromotionLevel

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
