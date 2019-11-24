package net.nemerosa.ontrack.kdsl.core

import net.nemerosa.ontrack.kdsl.client.GraphQLResponse

/**
 * A resource in Ontrack, based on JSON data.
 */
abstract class Resource : Connector() {

    /**
     * GraphQL query
     *
     * @param queryName Name to give to the query
     */
    @Deprecated("Use REST API end points")
    fun String.graphQLQuery(
            queryName: String,
            vararg params: GraphQLParamImpl
    ): GraphQLResponse {
        // Filter
        val filterDecl = if (params.isNotEmpty()) {
            "(${params.joinToString(",") { "${'$'}${it.name}: ${it.type}" }})"
        } else {
            ""
        }
        val filterVariables = params.associate { it.name to it.value }
        // GraphQL query
        val query = """
            query $queryName$filterDecl{
                $this
            }
        """.trimIndent()
        // Runs and parses the GraphQL query
        return ontrackConnector.graphQL(query, filterVariables)
    }

    /**
     * GraphQL parameter complete declaration
     */
    class GraphQLParamDecl(
            val name: String,
            val type: String
    )

    /**
     * Creating a parameter declaration
     */
    infix fun String.type(type: String) = GraphQLParamDecl(this, type)

    /**
     * GraphQL parameter complete implementation
     */
    class GraphQLParamImpl(
            val name: String,
            val type: String,
            val value: Any?
    )

    /**
     * Creating a parameter implementation
     */
    infix fun GraphQLParamDecl.value(value: Any?) = GraphQLParamImpl(this.name, this.type, value)
}
