package net.nemerosa.ontrack.ui.graphql.dsl.root.mutation

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class RootMutationProperty<I : Any, O : Any>(
        val input: Pair<String, KClass<I>>,
        val code: (I) -> O
) : ReadOnlyProperty<Any, DelegatedRootMutationGraphQLContributor<I, O>> {

    override fun getValue(thisRef: Any, property: KProperty<*>): DelegatedRootMutationGraphQLContributor<I, O> =
            DelegatedRootMutationGraphQLContributor(
                    property.name,
                    input,
                    code
            )

}

