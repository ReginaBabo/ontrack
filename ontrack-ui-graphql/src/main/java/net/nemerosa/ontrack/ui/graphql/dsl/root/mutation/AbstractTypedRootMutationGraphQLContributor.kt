package net.nemerosa.ontrack.ui.graphql.dsl.root.mutation

import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.TypeRuntimeWiring

/**
 * Contributes fields to the root `Mutation` using a typed input and output.
 */
abstract class AbstractTypedRootMutationGraphQLContributor<I : Any, O : Any> : RootMutationGraphQLContributor {

    /**
     * Name of the field in the root mutation
     */
    abstract val name: String

    /**
     * Converts the environment into the input type
     */
    abstract fun parseInput(env: DataFetchingEnvironment): I

    /**
     * Gets the value from the input
     */
    abstract fun getValue(input: I): O

    override fun contribute(builder: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder {
        return builder.dataFetcher(name) { env ->
            val input = parseInput(env)
            getValue(input)
        }
    }

}