package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.TypeRuntimeWiring
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
            // TODO Filter
            structureService.projectList
        }
    }
}