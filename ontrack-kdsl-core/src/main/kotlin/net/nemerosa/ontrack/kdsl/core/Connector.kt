package net.nemerosa.ontrack.kdsl.core

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.client.GraphQLResponse
import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * Object which gets an internal link to Ontrack through an [OntrackConnector].
 */
abstract class Connector(
        protected val ontrackConnector: OntrackConnector
) {

    protected inline fun <reified T> postAndParseAsObject(path: String, payload: Any?): T =
            ontrackConnector.post(path, payload)
                    ?.parse()
                    ?: throw MissingResponseException()

    protected inline fun <reified T> postAndConvert(path: String, payload: Any?, mapper: (JsonNode) -> T): T =
            ontrackConnector.post(path, payload)
                    ?.let { mapper(it) }
                    ?: throw MissingResponseException()

    protected fun <T> getResources(path: String, mapper: (JsonNode) -> T): List<T> =
            ontrackConnector.get(path)
                    ?.resources
                    ?.map(mapper)
                    ?: throw MissingResponseException()

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
