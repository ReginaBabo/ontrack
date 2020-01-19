package net.nemerosa.ontrack.kdsl.impl

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Label
import net.nemerosa.ontrack.dsl.LabelProviderDescription
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.support.jsonObject
import net.nemerosa.ontrack.kdsl.core.support.jsonOptionalText
import net.nemerosa.ontrack.kdsl.core.support.jsonText

class KDSLLabel(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLEntity(json, ontrackConnector), Label {
    override val category: String? by jsonOptionalText(json)
    override val name: String by jsonText(json)
    override val description: String? by jsonOptionalText(json)
    override val color: String by jsonText(json)
    override val computedBy: LabelProviderDescription? by jsonObject(json)
    override val foregroundColor: String by jsonText(json)
    override val display: String by jsonText(json)
}