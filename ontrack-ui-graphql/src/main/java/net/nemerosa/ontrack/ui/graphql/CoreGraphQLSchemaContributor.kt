package net.nemerosa.ontrack.ui.graphql

import org.springframework.stereotype.Component

@Component
class CoreGraphQLSchemaContributor : GraphQLSchemaContributor {

    override val schemaResourcePath: String = "/core.graphqls"

}