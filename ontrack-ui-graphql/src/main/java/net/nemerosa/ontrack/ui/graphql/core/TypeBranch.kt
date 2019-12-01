package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.buildfilter.BuildFilterProviderData
import net.nemerosa.ontrack.model.buildfilter.BuildFilterService
import net.nemerosa.ontrack.model.structure.Branch
import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class TypeBranch(
        private val structureService: StructureService,
        private val buildFilterService: BuildFilterService,
        typeFieldContributors: List<TypeFieldContributor>
) : AbstractTypeProjectEntity<Branch>(Branch::class, typeFieldContributors) {
    override fun dataFetchers(builder: TypeRuntimeWiring.Builder) {
        super.dataFetchers(builder)

        builder.field("promotionLevels") { branch ->
            structureService.getPromotionLevelListForBranch(branch.id)
        }

        builder.field<BranchBuildsInput, List<Build>>("builds") { branch, (count, lastPromotions) ->
            val buildFilter: BuildFilterProviderData<*> = when {
                // Last promotion filter
                lastPromotions != null && lastPromotions -> buildFilterService.lastPromotedBuildsFilterData()
                // TODO Standard filter
                // TODO Generic filter
                else -> buildFilterService.standardFilterProviderData(count ?: 10).build()
            }
            // Fetching the results
            buildFilter.filterBranchBuilds(branch)
        }

    }
}

data class BranchBuildsInput(
        val count: Int?,
        val lastPromotions: Boolean?
)
