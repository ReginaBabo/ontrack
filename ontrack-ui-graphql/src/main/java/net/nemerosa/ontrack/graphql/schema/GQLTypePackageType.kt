package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.refField
import net.nemerosa.ontrack.graphql.support.stringField
import net.nemerosa.ontrack.model.structure.PackageType
import org.springframework.stereotype.Component

@Component
class GQLTypePackageType(
        private val extensionFeatureDescription: GQLTypeExtensionFeatureDescription
) : GQLType {

    override fun getTypeName(): String = PackageType::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Package type")
                    .stringField("id", "Identifier for the package type")
                    .stringField("name", "Name of the package type")
                    .stringField("description", "Description of the package type")
                    .refField("feature", "Extension feature description", extensionFeatureDescription)
                    .build()

}