package net.nemerosa.ontrack.boot.graphql

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetcher
import graphql.schema.GraphQLFieldDefinition
import net.nemerosa.ontrack.graphql.schema.GQLProjectEntityFieldContributor
import net.nemerosa.ontrack.graphql.support.GQLScalarJSON
import net.nemerosa.ontrack.json.ObjectMapperFactory
import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.ProjectEntityType
import net.nemerosa.ontrack.ui.controller.URIBuilder
import net.nemerosa.ontrack.ui.resource.DefaultResourceContext
import net.nemerosa.ontrack.ui.resource.ResourceModule
import net.nemerosa.ontrack.ui.resource.ResourceObjectMapperFactory
import org.springframework.stereotype.Component

@Component
class GQLProjectEntityJsonFieldContributor(
        uriBuilder: URIBuilder,
        securityService: SecurityService,
        resourceModules: List<ResourceModule>
) : GQLProjectEntityFieldContributor {

    private val resourceMapper: ObjectMapper

    init {
        // Resource context
        val resourceContext = DefaultResourceContext(uriBuilder, securityService)
        // Object mapper
        val mapper = ObjectMapperFactory.create()
        // Resource mapper
        val resourceObjectMapper = ResourceObjectMapperFactory(mapper).resourceObjectMapper(
                resourceModules,
                resourceContext
        )
        // Using the corresponding mapper
        resourceMapper = resourceObjectMapper.objectMapper
    }

    override fun getFields(
            projectEntityClass: Class<out ProjectEntity>,
            projectEntityType: ProjectEntityType
    ): List<GraphQLFieldDefinition>? {
        return listOf(
                GraphQLFieldDefinition.newFieldDefinition()
                        .name("json")
                        .description("JSON representation of this entity, compatible with REST.")
                        .type(GQLScalarJSON.INSTANCE)
                        .dataFetcher(jsonFetcher())
                        .build()
        )
    }

    private fun jsonFetcher() = DataFetcher<JsonNode> { env ->
        resourceMapper.valueToTree<JsonNode>(env.getSource<ProjectEntity>())
    }

}