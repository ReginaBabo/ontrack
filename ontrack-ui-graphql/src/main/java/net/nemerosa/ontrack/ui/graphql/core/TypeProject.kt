package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.common.and
import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.parse
import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class TypeProject(
        private val structureService: StructureService,
        private val branchFavouriteService: BranchFavouriteService,
        private val branchModelMatcherService: BranchModelMatcherService
) : AbstractTypeProjectEntity<Project>(Project::class) {
    override fun dataFetchers(builder: TypeRuntimeWiring.Builder) {
        builder.dataFetcher("branches") { environment ->
            val project: Project = environment.getSource()
            val (name, favourite, useModel) = environment.parse<BranchListInput>()
            // Combined filter
            var filter: (Branch) -> Boolean = { true }
            // Name criteria
            if (name != null) {
                val nameFilter = Pattern.compile(name)
                filter = filter.and { branch -> nameFilter.matcher(branch.name).matches() }
            }
            // Favourite
            if (favourite != null && favourite) {
                filter = filter and { branchFavouriteService.isBranchFavourite(it) }
            }
            // Matching the branching model
            if (useModel != null && useModel) {
                val branchModelMatcher = branchModelMatcherService.getBranchModelMatcher(project)
                if (branchModelMatcher != null) {
                    filter = filter and { branchModelMatcher.matches(it) }
                }
            }
            // Result
            structureService
                    .getBranchesForProject(project.id)
                    .filter(filter)
        }
    }
}