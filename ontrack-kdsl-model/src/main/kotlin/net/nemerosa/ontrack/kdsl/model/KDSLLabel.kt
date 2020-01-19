package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Label
import net.nemerosa.ontrack.dsl.LabelProviderDescription
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.description
import net.nemerosa.ontrack.kdsl.core.getOptionalText
import net.nemerosa.ontrack.kdsl.core.getText
import net.nemerosa.ontrack.kdsl.core.parse

class KDSLLabel(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLEntity(json, ontrackConnector), Label {

    override val category: String? by lazy { json.getOptionalText("category") }
    override val name: String by lazy { json["name"].textValue() }
    override val description: String? by lazy { json.description }
    override val color: String by lazy { json.getText("color") }
    override val computedBy: LabelProviderDescription? by lazy {
        if (json.hasNonNull("computedBy")) {
            json.get("computedBy").parse<LabelProviderDescription>()
        } else {
            null
        }
    }
    override val foregroundColor: String by lazy { json.getText("foregroundColor") }
    override val display: String by lazy { json.getText("display") }
}