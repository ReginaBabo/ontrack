package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.common.getOrNull
import net.nemerosa.ontrack.model.exceptions.BranchNotFoundException
import net.nemerosa.ontrack.model.exceptions.ProjectNotFoundException
import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.root.query.AbstractTypedRootQueryGraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.parse
import org.springframework.stereotype.Component

@Component
class QueryBuilds(
        private val structureService: StructureService
) : AbstractTypedRootQueryGraphQLContributor<RootBuildListInput, List<Build>>() {

    override val name: String = "builds"

    override fun parseInput(env: DataFetchingEnvironment): RootBuildListInput = env.parse()

    override fun getValue(input: RootBuildListInput): List<Build> {
        val (id, projectName, branchName) = input
        // Per ID
        return if (id != null) {
            listOf(
                    structureService.getBuild(ID.of(id))
            )
        } else if (!projectName.isNullOrBlank()) {
            // ... and branch
            if (!branchName.isNullOrBlank()) {
                // Gets the branch
                val branch = structureService
                        .findBranchByName(projectName, branchName)
                        .getOrNull()
                        ?: throw BranchNotFoundException(projectName, branchName)
                // TODO Configurable branch filter
                // val filter: BuildFilterProviderData<*> = inputBuildStandardFilter.convert(branchFilter)
                // Runs the filter
                // filter.filterBranchBuilds(branch)
                TODO("Branch filter")
            } else {
                // Gets the project
                val project = structureService
                        .findProjectByName(projectName)
                        .getOrNull()
                        ?: throw ProjectNotFoundException(projectName)
                // TODO Build search form as argument
                // val form: BuildSearchForm = inputBuildSearchForm.convert(projectFilter)
                // structureService.buildSearch(project.id, form)
                TODO("Project filter")
            }
            // TODO Branch filter
//       } else if (branchFilter != null) {
//            throw IllegalStateException(String.format(
//                    "%s must be used together with %s",
//                    GQLRootQueryBuilds.BUILD_BRANCH_FILTER_ARGUMENT,
//                    GQLRootQueryBuilds.BRANCH_ARGUMENT
//            ))
            // TODO Project filter
//        } else if (projectFilter != null) {
//            throw IllegalStateException(String.format(
//                    "%s must be used together with %s",
//                    GQLRootQueryBuilds.BUILD_PROJECT_FILTER_ARGUMENT,
//                    GQLRootQueryBuilds.PROJECT_ARGUMENT
//            ))
        } else {
            emptyList()
        }
    }
}