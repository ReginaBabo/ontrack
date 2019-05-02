package net.nemerosa.ontrack.graphql.schema

import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.support.objectArrayField
import net.nemerosa.ontrack.model.chart.Chart
import org.springframework.stereotype.Component

@Component
class GQLTypeChart(
        private val chartSeries: GQLTypeChartSeries
) : GQLType {

    override fun getTypeName(): String = Chart::class.java.simpleName

    override fun createType(cache: GQLTypeCache): GraphQLObjectType =
            GraphQLObjectType.newObject()
                    .name(typeName)
                    .description("Abstract chart representation")
                    .objectArrayField(chartSeries, "series", "List of series in this chart")
                    .build()

}