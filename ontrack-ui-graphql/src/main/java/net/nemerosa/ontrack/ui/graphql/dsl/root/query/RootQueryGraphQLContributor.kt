package net.nemerosa.ontrack.ui.graphql.dsl.root.query

import graphql.schema.idl.TypeRuntimeWiring

/**
 * Contributes fields to the root [`Query`][RootQueryGraphQL].
 */
interface RootQueryGraphQLContributor {

    fun contribute(builder: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder

}