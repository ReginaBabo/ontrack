package net.nemerosa.ontrack.graphql.schema

import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.GraphqlUtils.stdList
import net.nemerosa.ontrack.graphql.support.stringField
import net.nemerosa.ontrack.model.structure.BuildPackageVersionParser
import org.springframework.stereotype.Component

@Component
class GQLTypeBuildPackageVersionParser : GQLType {

    override fun getTypeName(): String = BuildPackageVersionParser::class.java.simpleName

    override fun createType(cache: GQLTypeCache?): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Parser for build package versions")
                    .stringField("name", "Parser name")
                    .stringField("description", "Description of the parser")
                    .stringField("mimeType", "Preferred MIME type")
                    .field {
                        it.name("mimeTypes")
                                .description("ALl supported MIME types")
                                .type(stdList(GraphQLString))
                    }
                    .stringField("example", "Example of file")
                    .build()

}