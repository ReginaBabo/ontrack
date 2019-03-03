package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import net.nemerosa.ontrack.graphql.support.GraphqlUtils.stdList
import net.nemerosa.ontrack.model.structure.BuildPackageVersionParser
import org.springframework.stereotype.Component

@Component
class GQLRootQueryPackageVersionParsers(
        private val parser: GQLTypeBuildPackageVersionParser,
        private val parsers: List<BuildPackageVersionParser>
) : GQLRootQuery {
    override fun getFieldDefinition(): GraphQLFieldDefinition =
            GraphQLFieldDefinition.newFieldDefinition()
                    .name("buildPackageVersionParsers")
                    .description("List of parsers for build package versions")
                    .type(stdList(parser.typeRef))
                    .dataFetcher { parsers }
                    .build()
}