package net.nemerosa.ontrack.ui.graphql

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import net.nemerosa.ontrack.graphql.support.GQLScalarJSON
import net.nemerosa.ontrack.ui.graphql.support.GQLScalarID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component


@Component
class GraphQLProvider(
        private val contributors: List<GraphQLContributor>
) {

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

        val coreSchemaSource = javaClass.getResourceAsStream("/core.graphqls").reader().readText()

        val typeRegistry = TypeDefinitionRegistry()
        typeRegistry.merge(schemaParser.parse(coreSchemaSource))
        // TODO Merge schemas provided by extensions

        return schemaGenerator.makeExecutableSchema(typeRegistry, buildRuntimeWiring())
    }

    private fun buildRuntimeWiring(): RuntimeWiring {
        val wiring = RuntimeWiring.newRuntimeWiring()
                // Scalars
                .scalar(GQLScalarJSON.INSTANCE)
                .scalar(GQLScalarID.INSTANCE)
        // Gets all contributors
        contributors.forEach { contributor ->
            contributor.wire(wiring)
        }
        // OK
        return wiring.build()
    }

}