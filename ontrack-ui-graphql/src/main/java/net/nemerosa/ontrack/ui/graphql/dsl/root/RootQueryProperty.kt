package net.nemerosa.ontrack.ui.graphql.dsl.root

import graphql.schema.DataFetchingEnvironment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RootQueryProperty<I : Any, O : Any>(
        val parser: (DataFetchingEnvironment) -> I,
        val code: (I) -> O
) : ReadOnlyProperty<Any, DelegatedRootQueryGraphQLContributor<I, O>> {

    override fun getValue(thisRef: Any, property: KProperty<*>): DelegatedRootQueryGraphQLContributor<I, O> {
        val name = property.name
        return DelegatedRootQueryGraphQLContributor(
                property.name,
                parser,
                code
        )
    }

}

