package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.ui.graphql.GraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.typedDataFetcher
import kotlin.reflect.KClass

abstract class AbstractType<T : Any>(
        private val type: KClass<T>
) : GraphQLContributor {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type(type.java.simpleName) {
            dataFetchers(it)
            it
        }
    }

    abstract fun dataFetchers(builder: TypeRuntimeWiring.Builder)

    protected fun <O> TypeRuntimeWiring.Builder.field(
            name: String,
            valueFn: (T) -> O
    ) = typedDataFetcher(name, valueFn)

    protected inline fun <reified I : Any, O> TypeRuntimeWiring.Builder.field(
            name: String,
            noinline valueFn: (T, I) -> O
    ) = typedDataFetcher(name, valueFn)

}
