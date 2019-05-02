package net.nemerosa.ontrack.model.chart

import com.fasterxml.jackson.databind.JsonNode

class ChartPoint(
        val ref: ChartRef,
        val status: String?,
        val duration: Int?,
        val data: JsonNode?
)
