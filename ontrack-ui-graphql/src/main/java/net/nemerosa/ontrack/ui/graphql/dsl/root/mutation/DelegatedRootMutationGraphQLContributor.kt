package net.nemerosa.ontrack.ui.graphql.dsl.root.mutation

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.ui.graphql.dsl.support.get
import kotlin.reflect.KClass

class DelegatedRootMutationGraphQLContributor<I : Any, O : Any>(
        override val name: String,
        private val input: Pair<String, KClass<I>>,
        private val valueFn: (I) -> O
) : AbstractTypedRootMutationGraphQLContributor<I, O>() {

    override fun parseInput(env: DataFetchingEnvironment): I = env.get(input.first, input.second)

    override fun getValue(input: I): O = valueFn(input)
}