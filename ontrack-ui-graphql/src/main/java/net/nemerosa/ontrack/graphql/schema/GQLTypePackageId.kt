package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.GraphQLBeanConverter
import net.nemerosa.ontrack.model.structure.PackageId
import org.springframework.stereotype.Component

@Component
class GQLTypePackageId : GQLType {
    override fun getTypeName(): String = PackageId::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLBeanConverter.asObjectTypeBuilder(PackageId::class.java, cache, emptySet())
                    // OK
                    .build()
}