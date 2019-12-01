package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import org.springframework.stereotype.Component

@Component
class TypeProject : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type("Project") {
            it.dataFetcher("id") { environment ->
                val project: Project = environment.getSource()
                project.id.value
            }
        }
    }
}