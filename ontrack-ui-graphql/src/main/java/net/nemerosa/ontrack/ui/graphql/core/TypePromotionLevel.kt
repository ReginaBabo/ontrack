package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import net.nemerosa.ontrack.graphql.support.GraphqlUtils
import net.nemerosa.ontrack.model.structure.PromotionLevel
import net.nemerosa.ontrack.model.structure.PromotionRun
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import org.springframework.stereotype.Component

@Component
class TypePromotionLevel(
        private val structureService: StructureService
) : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type("PromotionLevel") {
            it.dataFetcher("id") { environment ->
                val promotionLevel: PromotionLevel = environment.getSource()
                promotionLevel.id.value
            }

            it.dataFetcher("promotionRuns") { environment ->
                val promotionLevel: PromotionLevel = environment.getSource()
                // Gets all the promotion runs
                val promotionRuns: List<PromotionRun> = structureService.getPromotionRunsForPromotionLevel(promotionLevel.id)
                // Filters according to the arguments
                GraphqlUtils.stdListArgumentsFilter(promotionRuns, environment)
            }
        }
    }
}
