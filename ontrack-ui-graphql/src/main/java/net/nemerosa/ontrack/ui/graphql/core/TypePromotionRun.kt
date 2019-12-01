package net.nemerosa.ontrack.ui.graphql.core

import net.nemerosa.ontrack.model.structure.PromotionRun
import org.springframework.stereotype.Component

@Component
class TypePromotionRun : AbstractTypeProjectEntity<PromotionRun>(PromotionRun::class) {
}
