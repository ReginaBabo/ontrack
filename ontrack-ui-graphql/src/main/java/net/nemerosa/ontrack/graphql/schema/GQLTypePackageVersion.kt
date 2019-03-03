package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.refField
import net.nemerosa.ontrack.graphql.support.stringField
import net.nemerosa.ontrack.model.structure.PackageVersion
import org.springframework.stereotype.Component

@Component
class GQLTypePackageVersion(
        private val packageId: GQLTypePackageId
) : GQLType {

    override fun getTypeName(): String = PackageVersion::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Package version")
                    .refField("packageId", "Package ID", packageId)
                    .stringField("version", "Version for the package")
                    .build()

}