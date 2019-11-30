package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.RuntimeWiring
import net.nemerosa.ontrack.model.structure.Project
import org.springframework.stereotype.Component

@Component
class ProjectGraphQL : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type("Project") {
            it.dataFetcher("id") { environment ->
                val project: Project = environment.getSource()
                project.id.value
            }
        }
    }
}