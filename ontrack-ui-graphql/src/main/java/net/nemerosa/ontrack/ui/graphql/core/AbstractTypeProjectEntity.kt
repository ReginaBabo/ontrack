package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import kotlin.reflect.KClass

abstract class AbstractTypeProjectEntity<T : ProjectEntity>(
        private val type: KClass<T>
) : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type(type.java.simpleName) {
            it.dataFetcher("id") { environment ->
                val entity: ProjectEntity = environment.getSource()
                entity.id.value
            }
            // Additional fields
            dataFetchers(it)
            // OK
            it
        }
    }

    abstract fun dataFetchers(builder: TypeRuntimeWiring.Builder)
}
