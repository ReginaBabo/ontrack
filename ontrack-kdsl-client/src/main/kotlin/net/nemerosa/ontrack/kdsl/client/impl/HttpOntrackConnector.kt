package net.nemerosa.ontrack.kdsl.client.impl

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.client.GraphQLResponse
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class HttpOntrackConnector(
        val url: String,
        val username: String?,
        password: String?
) : OntrackConnector {

    private val restTemplate = RestTemplateBuilder()
            .rootUri(url)
            .run {
                if (username != null && password != null) {
                    basicAuthentication(username, password)
                } else {
                    this
                }
            }
            .build()

    override fun asAnonymous(): OntrackConnector =
            HttpOntrackConnector(url = url, username = null, password = null)

    override fun download(path: String): ByteArray {
        return restTemplate.getForObject(
                "/$path",
                ByteArray::class.java
        ) ?: throw RuntimeException("Cannot download anything at $path")
    }

    override fun get(path: String): JsonNode? {
        return try {
            restTemplate.getForObject(
                    "/$path",
                    JsonNode::class.java
            )
        } catch (ex: HttpClientErrorException) {
            if (ex.statusCode == HttpStatus.NOT_FOUND) {
                null
            } else {
                throw ex
            }
        }
    }

    override fun post(path: String, payload: Any?): JsonNode? =
            restTemplate.postForObject(
                    "/$path",
                    payload,
                    JsonNode::class.java
            )

    override fun put(path: String, payload: Any) {
        restTemplate.put(
                "/$path",
                payload,
                JsonNode::class.java
        )
    }

    override fun delete(path: String) {
        restTemplate.delete(
                "/$path"
        )
    }

    override fun graphQL(query: String, variables: Map<String, Any?>): GraphQLResponse {
        return restTemplate.postForObject(
                "/graphql",
                mapOf(
                        "query" to query,
                        "variables" to variables
                ),
                GraphQLResponse::class.java
        ) ?: throw RuntimeException("Cannot get any response from GraphQL end point")
    }
}