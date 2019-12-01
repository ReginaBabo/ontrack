package net.nemerosa.ontrack.ui.graphql.core

import net.nemerosa.ontrack.model.structure.PromotionRun
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import org.springframework.stereotype.Component

@Component
class TypePromotionRun(
        typeFieldContributors: List<TypeFieldContributor>
) : AbstractTypeProjectEntity<PromotionRun>(PromotionRun::class, typeFieldContributors)
