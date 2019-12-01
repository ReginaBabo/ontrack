package net.nemerosa.ontrack.extension.general.graphql

import net.nemerosa.ontrack.ui.graphql.GraphQLSchemaContributor
import org.springframework.stereotype.Component

@Component
class GeneralGraphQLSchemaContributor : GraphQLSchemaContributor {
    override val schemaResourcePath: String = "/general.graphqls"
}