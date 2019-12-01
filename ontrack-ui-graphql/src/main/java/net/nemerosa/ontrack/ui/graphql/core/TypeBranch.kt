package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import net.nemerosa.ontrack.model.structure.Branch
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import org.springframework.stereotype.Component

@Component
class TypeBranch(
        private val structureService: StructureService
) : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type("Branch") {
            it.dataFetcher("id") { environment ->
                val branch: Branch = environment.getSource()
                branch.id.value
            }

            it.dataFetcher("promotionLevels") { environment ->
                val branch: Branch = environment.getSource()
                structureService.getPromotionLevelListForBranch(branch.id)
            }
        }
    }
}