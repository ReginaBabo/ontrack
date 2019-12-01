package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.common.and
import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.ui.graphql.dsl.root.query.AbstractTypedRootQueryGraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.parse
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class QueryBranches(
        private val structureService: StructureService,
        private val branchFavouriteService: BranchFavouriteService
) : AbstractTypedRootQueryGraphQLContributor<RootBranchListInput, List<Branch>>() {

    override val name: String = "branches"

    override fun parseInput(env: DataFetchingEnvironment): RootBranchListInput = env.parse()

    override fun getValue(input: RootBranchListInput): List<Branch> {
        val (id, project, name, favourite) = input

        // Per ID
        return if (id != null) {
            listOf(structureService.getBranch(ID.of(id)))
        } else if ((favourite != null && favourite) || StringUtils.isNotBlank(project) || StringUtils.isNotBlank(name)) {

            // Project filter
            var projectFilter: (Project) -> Boolean = { true }
            if (StringUtils.isNotBlank(project)) {
                projectFilter = projectFilter.and { p -> project == p.name }
            }

            // Branch filter
            var branchFilter: (Branch) -> Boolean = { true }
            if (!name.isNullOrBlank()) {
                val pattern = Pattern.compile(name)
                branchFilter = branchFilter.and { b -> pattern.matcher(b.name).matches() }
            }
            if (favourite != null && favourite) {
                branchFilter = branchFilter and { branchFavouriteService.isBranchFavourite(it) }
            }

            // TODO Property filter?
//            if (propertyFilterArg != null) {
//                val filterObject: PropertyFilter? = propertyFilter.convert(propertyFilterArg)
//                val type = filterObject?.type
//                if (filterObject != null && type != null && type.isNotBlank()) {
//                    branchFilter = branchFilter.and { b -> propertyFilter.getFilter(filterObject).test(b) }
//                }
//            }

            // Gets the list of authorised projects
            structureService.projectList
                    // Filter on the project
                    .filter(projectFilter)
                    // Gets the list of branches
                    .flatMap { prj -> structureService.getBranchesForProject(prj.id) }
                    // Filter on the branch
                    .filter(branchFilter)
        } else {
            emptyList()
        }
    }
}