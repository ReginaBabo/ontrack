package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.objectArrayField
import net.nemerosa.ontrack.graphql.support.stringField
import net.nemerosa.ontrack.model.chart.ChartSeries
import org.springframework.stereotype.Component

@Component
class GQLTypeChartSeries(
        private val chartPoint: GQLTypeChartPoint
) : GQLType {

    override fun getTypeName(): String = ChartSeries::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Series of points on a graph")
                    .stringField("type", "Type of series")
                    .stringField("name", "Qualifier for the type of series")
                    .objectArrayField(chartPoint, "points", "List of point in the series")
                    .build()

}