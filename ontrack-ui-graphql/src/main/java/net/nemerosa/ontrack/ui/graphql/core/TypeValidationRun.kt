package net.nemerosa.ontrack.ui.graphql.core

import net.nemerosa.ontrack.model.structure.ValidationRun
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import org.springframework.stereotype.Component

@Component
class TypeValidationRun(
        typeFieldContributors: List<TypeFieldContributor>
) : AbstractTypeProjectEntity<ValidationRun>(ValidationRun::class, typeFieldContributors)
