package net.nemerosa.ontrack.ui.graphql

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import net.nemerosa.ontrack.graphql.support.GQLScalarJSON
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component


@Component
class GraphQLProvider(
        private val contributors: List<GraphQLContributor>,
        private val schemaContributors: List<GraphQLSchemaContributor>
) {

    private val logger: Logger = LoggerFactory.getLogger(GraphQLProvider::class.java)

    @Bean
    @Lazy
    fun graphQL(): GraphQL = buildGraphQL()

    private fun buildGraphQL(): GraphQL {
        val graphQLSchema = buildSchema()
        return GraphQL.newGraphQL(graphQLSchema).build()
    }

    private fun buildSchema(): GraphQLSchema {
        val schemaParser = SchemaParser()
        val schemaGenerator = SchemaGenerator()

        val resourcePaths = schemaContributors.map {
            it.schemaResourcePath
        }

        val schemaSources = resourcePaths.map { path ->
            logger.info("[graphql] schema = $path")
            javaClass.getResourceAsStream(path).reader().readText()
        }

        val typeRegistry = TypeDefinitionRegistry()
        schemaSources.forEach {
            typeRegistry.merge(schemaParser.parse(it))
        }

        return schemaGenerator.makeExecutableSchema(typeRegistry, buildRuntimeWiring())
    }

    private fun buildRuntimeWiring(): RuntimeWiring {
        val wiring = RuntimeWiring.newRuntimeWiring()
                // Scalars
                .scalar(GQLScalarJSON.INSTANCE)
        // Gets all contributors
        contributors.forEach { contributor ->
            contributor.wire(wiring)
        }
        // OK
        return wiring.build()
    }

}