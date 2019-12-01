package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.Branch
import net.nemerosa.ontrack.model.structure.StructureService
import org.springframework.stereotype.Component

@Component
class TypeBranch(
        private val structureService: StructureService
) : AbstractTypeProjectEntity<Branch>(Branch::class) {
    override fun dataFetchers(builder: TypeRuntimeWiring.Builder) {
        super.dataFetchers(builder)
        builder.field("promotionLevels") { branch ->
            structureService.getPromotionLevelListForBranch(branch.id)
        }

    }
}