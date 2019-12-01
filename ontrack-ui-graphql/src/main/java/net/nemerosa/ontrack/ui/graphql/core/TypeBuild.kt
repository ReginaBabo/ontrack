package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.PromotionLevel
import net.nemerosa.ontrack.model.structure.PromotionRun
import net.nemerosa.ontrack.model.structure.StructureService
import org.springframework.stereotype.Component

@Component
class TypeBuild(
        private val structureService: StructureService
) : AbstractTypeProjectEntity<Build>(Build::class) {
    override fun dataFetchers(builder: TypeRuntimeWiring.Builder) {
        super.dataFetchers(builder)
        promotionRuns(builder)
    }

    private fun promotionRuns(builder: TypeRuntimeWiring.Builder) {
        builder.field<BuildPromotionRunsInput, List<PromotionRun>>("promotionRuns") { build, (promotion, lastPerLevel) ->
            val promotionLevel: PromotionLevel? = if (promotion != null) {
                // Gets the promotion level
                structureService.findPromotionLevelByName(
                        build.project.name,
                        build.branch.name,
                        promotion
                ).orElse(null)
            } else {
                null
            }
            if (promotionLevel != null) {
                // Gets promotion runs for this promotion level
                if (lastPerLevel != null && lastPerLevel) {
                    structureService.getLastPromotionRunForBuildAndPromotionLevel(build, promotionLevel)
                            .map { listOf(it) }
                            .orElse(listOf())
                } else {
                    structureService.getPromotionRunsForBuildAndPromotionLevel(build, promotionLevel)
                }
            } else {
                // Gets all the promotion runs
                if (lastPerLevel != null && lastPerLevel) {
                    structureService.getLastPromotionRunsForBuild(build.id)
                } else {
                    structureService.getPromotionRunsForBuild(build.id)
                }
            }
        }
    }
}

data class BuildPromotionRunsInput(
        val promotion: String?,
        val lastPerLevel: Boolean?
)
