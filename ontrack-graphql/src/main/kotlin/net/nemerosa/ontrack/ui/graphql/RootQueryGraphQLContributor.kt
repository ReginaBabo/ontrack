package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.TypeRuntimeWiring

/**
 * Contributes fields to the root [`Query`][RootQueryGraphQL].
 */
interface RootQueryGraphQLContributor {

    fun contribute(builder: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder

}