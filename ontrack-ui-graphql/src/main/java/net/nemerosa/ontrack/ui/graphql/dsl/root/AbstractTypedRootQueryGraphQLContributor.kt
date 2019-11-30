package net.nemerosa.ontrack.ui.graphql.dsl.root

import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.ui.graphql.RootQueryGraphQLContributor

/**
 * Contributes fields to the root [`Query`][RootQueryGraphQL] using a typed input and output.
 */
abstract class AbstractTypedRootQueryGraphQLContributor<I : Any, O : Any> : RootQueryGraphQLContributor {

    /**
     * Name of the field in the root query
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