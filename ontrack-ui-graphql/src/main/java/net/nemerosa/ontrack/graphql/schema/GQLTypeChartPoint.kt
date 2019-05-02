package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.objectField
import net.nemerosa.ontrack.graphql.support.stringField
import net.nemerosa.ontrack.model.chart.ChartPoint
import org.springframework.stereotype.Component

@Component
class GQLTypeChartPoint(
        private val chartRef: GQLTypeChartRef
) : GQLType {

    override fun getTypeName(): String = ChartPoint::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Point on a chart")
                    .objectField(chartRef, "ref", "Reference for the point")
                    .stringField("status", "Status for this point")
                    .stringField("duration", "Duration of execution for this point")
                    // TODO Data as JSON
                    .build()

}