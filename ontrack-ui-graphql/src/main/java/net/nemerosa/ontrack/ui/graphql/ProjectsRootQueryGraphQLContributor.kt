package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.StructureService
import org.springframework.stereotype.Component

@Component
class ProjectsRootQueryGraphQLContributor(
        private val structureService: StructureService
) : RootQueryGraphQLContributor {
    override fun contribute(builder: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder {
        return builder.dataFetcher("projects") { env ->
            val id: Int? = env.getArgument("id")
            val name: String? = env.getArgument("name")
            val favourites: Boolean? = env.getArgument("favourites")
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
}