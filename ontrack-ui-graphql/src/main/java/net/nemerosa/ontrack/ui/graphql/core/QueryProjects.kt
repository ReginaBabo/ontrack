package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.root.query.AbstractTypedRootQueryGraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.get
import org.springframework.stereotype.Component

@Component
class ProjectsRootQueryGraphQLContributor(
        private val structureService: StructureService
) : AbstractTypedRootQueryGraphQLContributor<ProjectsInput, List<Project>>() {

    override val name: String = "projects"

    override fun parseInput(env: DataFetchingEnvironment) = ProjectsInput(
            env.get(ProjectsInput::id),
            env.get(ProjectsInput::name),
            env.get(ProjectsInput::favourites)
    )

    override fun getValue(input: ProjectsInput): List<Project> {
        val (id, name, favourites) = input
        // By ID
        return if (id != null) {
            listOf(
                    structureService.getProject(ID.of(id))
            )
        }
        // By name
        else if (!name.isNullOrBlank()) {
            listOfNotNull(
                    structureService.findProjectByNameIfAuthorized(name)
            )
        }
        // Favorite projects
        else if (favourites != null && favourites) {
            structureService.projectFavourites
        }
        // TODO Property filter
//          val propertyFilterArg: Map<String, *>? = environment.getArgument(GQLInputPropertyFilter.ARGUMENT_NAME)
//          if (propertyFilterArg != null) {
//              val filterObject = propertyFilter.convert(propertyFilterArg)
//              if (filterObject != null && StringUtils.isNotBlank(filterObject.type)) {
//                  val propertyPredicate = propertyFilter.getFilter(filterObject)
//                  filter = filter and { propertyPredicate.test(it) }
//              }
//          }
        // Whole list
        else {
            structureService.projectList
        }
    }

}

data class ProjectsInput(
        val id: Int?,
        val name: String?,
        val favourites: Boolean?
        // TODO Property filter
)
