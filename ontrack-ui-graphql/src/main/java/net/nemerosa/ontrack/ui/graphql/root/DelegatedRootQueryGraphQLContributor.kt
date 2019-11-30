package net.nemerosa.ontrack.ui.graphql.root

import graphql.schema.DataFetchingEnvironment

class DelegatedRootQueryGraphQLContributor<I : Any, O : Any>(
        override val name: String,
        private val parser: (DataFetchingEnvironment) -> I,
        private val valueFn: (I) -> O
) : AbstractTypedRootQueryGraphQLContributor<I, O>() {

    override fun parseInput(env: DataFetchingEnvironment): I = parser(env)

    override fun getValue(input: I): O = valueFn(input)
}