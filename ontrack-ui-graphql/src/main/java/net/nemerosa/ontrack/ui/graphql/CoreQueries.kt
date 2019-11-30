package net.nemerosa.ontrack.ui.graphql

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.root.RootQueryDelegates.rootQuery
import net.nemerosa.ontrack.ui.graphql.dsl.support.get
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreQueries(
        private val structureService: StructureService
) {
    @get:Bean
    val projects by rootQuery<ProjectsInput, List<Project>>(ProjectsInput.Companion::parse) { (id, name, favourites) ->
        // By ID
        if (id != null) {
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

/**
 * Filters for a list of projects
 */
data class ProjectsInput(
        val id: Int?,
        val name: String?,
        val favourites: Boolean?
        // TODO Property filter
) {
    companion object {
        fun parse(env: DataFetchingEnvironment) = ProjectsInput(
                env.get(ProjectsInput::id),
                env.get(ProjectsInput::name),
                env.get(ProjectsInput::favourites)
        )
    }
}
