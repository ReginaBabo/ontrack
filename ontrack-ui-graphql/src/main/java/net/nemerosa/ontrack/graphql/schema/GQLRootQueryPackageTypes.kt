package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import net.nemerosa.ontrack.graphql.support.GraphqlUtils.stdList
import net.nemerosa.ontrack.model.structure.PackageService
import org.springframework.stereotype.Component

/**
 * Gets the list of labels
 */
@Component
class GQLRootQueryPackageTypes(
        private val packageType: GQLTypePackageType,
        private val packageService: PackageService
) : GQLRootQuery {
    override fun getFieldDefinition(): GraphQLFieldDefinition =
            GraphQLFieldDefinition.newFieldDefinition()
                    .name("packageTypes")
                    .description("List of all package types")
                    .type(stdList(packageType.typeRef))
                    .dataFetcher { packageService.packageTypes }
                    .build()
}
