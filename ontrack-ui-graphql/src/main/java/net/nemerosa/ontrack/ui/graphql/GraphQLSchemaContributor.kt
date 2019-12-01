package net.nemerosa.ontrack.ui.graphql

/**
 * Contributes to the general GraphQL schema.
 */
interface GraphQLSchemaContributor {

    /**
     * Path to a resource containing a GraphQL schema portion.
     */
    val schemaResourcePath: String

}