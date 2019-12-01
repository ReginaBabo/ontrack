package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.PromotionRun
import org.springframework.stereotype.Component

@Component
class TypePromotionRun : AbstractTypeProjectEntity<PromotionRun>(PromotionRun::class) {
    override fun dataFetchers(builder: TypeRuntimeWiring.Builder) {}
}
