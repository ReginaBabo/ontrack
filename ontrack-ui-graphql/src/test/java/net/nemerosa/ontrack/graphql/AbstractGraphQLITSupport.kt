package net.nemerosa.ontrack.graphql

import com.fasterxml.jackson.databind.JsonNode
import graphql.GraphQL
import net.nemerosa.ontrack.graphql.support.exception
import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import net.nemerosa.ontrack.json.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.fail

/**
 * Testing GraphQL queries.
 */
abstract class AbstractGraphQLITSupport : AbstractDSLTestSupport() {

    @Autowired
    private lateinit var graphQL: GraphQL

    fun run(query: String, variables: Map<String, *> = emptyMap<String, Any>()): JsonNode {
        val result = graphQL.execute {
            it.query(query).variables(variables)
        }
        val error = result.exception
        if (error != null) {
            throw error
        } else if (result.errors != null && result.errors.isNotEmpty()) {
            fail(result.errors.joinToString("\n") { it.message })
        } else {
            val data: Any? = result.getData()
            if (data != null) {
                return JsonUtils.format(data)
            } else {
                fail("No data was returned and no error was thrown.")
            }
        }
    }

}
