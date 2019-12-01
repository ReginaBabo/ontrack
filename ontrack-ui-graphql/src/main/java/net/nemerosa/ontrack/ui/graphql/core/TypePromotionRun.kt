package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import net.nemerosa.ontrack.model.structure.PromotionLevel
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import org.springframework.stereotype.Component

@Component
class TypePromotionRun : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type("PromotionRun") {
            it.dataFetcher("id") { environment ->
                val promotionLevel: PromotionLevel = environment.getSource()
                promotionLevel.id.value
            }
        }
    }
}
