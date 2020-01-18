package net.nemerosa.ontrack.kdsl.core

import net.nemerosa.ontrack.kdsl.client.GraphQLResponse
import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * Object which gets an internal link to Ontrack through an [OntrackConnector].
 */
abstract class Connector(
        protected val ontrackConnector: OntrackConnector
) {

    /**
     * GraphQL query
     *
     * @param queryName Name to give to the query
     */
    fun graphQLQuery(
            queryName: String,
            query: String,
            vararg params: GraphQLParamImpl) = graphQLQuery(queryName, query, params.toList())

    /**
     * GraphQL query
     *
     * @param queryName Name to give to the query
     */
    fun graphQLQuery(
            queryName: String,
            query: String,
            params: List<GraphQLParamImpl>
    ): GraphQLResponse {
        // Filter
        val filterDecl = if (params.isNotEmpty()) {
            "(${params.joinToString(",") { "${'$'}${it.name}: ${it.type}" }})"
        } else {
            ""
        }
        val filterVariables = params.associate { it.name to it.value }
        // GraphQL query
        val fullQuery = """
            query $queryName$filterDecl{
                $query
            }
        """
        // Runs and parses the GraphQL query
        return ontrackConnector.graphQL(fullQuery, filterVariables)
    }
}
