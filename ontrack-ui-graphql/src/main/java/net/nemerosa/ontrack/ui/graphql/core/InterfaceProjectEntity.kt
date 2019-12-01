package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import org.springframework.stereotype.Component

@Component
class InterfaceProjectEntity : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type(
                TypeRuntimeWiring
                        .newTypeWiring("ProjectEntity")
                        .typeResolver { env ->
                            val o = env.getObject<Any>()
                            if (o is ProjectEntity) {
                                val typeName = o::class.java.simpleName
                                env.schema.getObjectType(typeName)
                            } else {
                                throw IllegalStateException("Cannot map ${o::class} to ProjectEntity")
                            }
                        }
        )
    }
}