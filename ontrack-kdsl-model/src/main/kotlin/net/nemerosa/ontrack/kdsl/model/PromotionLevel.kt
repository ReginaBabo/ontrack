package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.model.ProjectEntity

interface PromotionLevel : ProjectEntity {
    val name: String
    val description: String
}
