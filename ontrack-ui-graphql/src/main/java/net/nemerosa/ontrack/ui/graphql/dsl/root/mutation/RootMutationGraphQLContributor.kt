package net.nemerosa.ontrack.ui.graphql.dsl.root.mutation

import graphql.schema.idl.TypeRuntimeWiring

/**
 * Contributes fields to the root [`Mutation`][RootMutationGraphQL].
 */
interface RootMutationGraphQLContributor {

    fun contribute(builder: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder

}