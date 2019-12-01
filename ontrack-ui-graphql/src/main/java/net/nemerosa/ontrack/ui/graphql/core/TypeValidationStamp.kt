package net.nemerosa.ontrack.ui.graphql.core

import net.nemerosa.ontrack.model.structure.ValidationStamp
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import org.springframework.stereotype.Component

@Component
class TypeValidationStamp(
        typeFieldContributors: List<TypeFieldContributor>
) : AbstractTypeProjectEntity<ValidationStamp>(ValidationStamp::class, typeFieldContributors)

