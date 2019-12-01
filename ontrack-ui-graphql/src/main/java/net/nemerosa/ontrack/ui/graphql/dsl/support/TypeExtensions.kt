package net.nemerosa.ontrack.ui.graphql.dsl.support

import graphql.schema.idl.TypeRuntimeWiring

fun <S, O> TypeRuntimeWiring.Builder.typedDataFetcher(
        name: String,
        valueFn: (S) -> O
) {
    dataFetcher(name) { env ->
        val source: S = env.getSource()
        valueFn(source)
    }
}


inline fun <S, reified I, O> TypeRuntimeWiring.Builder.typedDataFetcher(
        name: String,
        noinline valueFn: (S, I) -> O
) {
    dataFetcher(name) { env ->
        val source: S = env.getSource()
        val input: I = env.parse()
        valueFn(source, input)
    }
}
