package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.stringField
import net.nemerosa.ontrack.model.chart.ChartRef
import org.springframework.stereotype.Component

@Component
class GQLTypeChartRef : GQLType {

    override fun getTypeName(): String = ChartRef::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Reference for a chart (x-axis)")
                    .stringField("name", "Name of the reference")
                    .stringField("timestamp", "Timestamp of the reference")
                    .build()
}