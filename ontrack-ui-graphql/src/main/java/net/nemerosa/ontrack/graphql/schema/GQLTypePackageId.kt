package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.refField
import net.nemerosa.ontrack.graphql.support.stringField
import net.nemerosa.ontrack.model.structure.PackageId
import org.springframework.stereotype.Component

@Component
class GQLTypePackageId(
        private val packageType: GQLTypePackageType
) : GQLType {

    override fun getTypeName(): String = PackageId::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Package identifier")
                    .stringField("id", "Identifier for the package")
                    .refField("type", "Package type", packageType)
                    .build()

}