package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.refField
import net.nemerosa.ontrack.model.structure.BuildPackageVersion
import org.springframework.stereotype.Component

@Component
class GQLTypeBuildPackageVersion(
        private val packageVersion: GQLTypePackageVersion
) : GQLType {

    override fun getTypeName(): String = BuildPackageVersion::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Package version with optional linked build")
                    .refField("packageVersion", "Package version", packageVersion)
                    .refField("target", "Linked build", GQLTypeBuild.BUILD)
                    .build()
}