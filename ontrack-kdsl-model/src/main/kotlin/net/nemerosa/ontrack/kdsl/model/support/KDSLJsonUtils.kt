package net.nemerosa.ontrack.kdsl.model.support

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Signature
import net.nemerosa.ontrack.kdsl.core.getInt

val JsonNode.id: Int get() = getInt("id")

val JsonNode.name: String get() = this["name"].textValue()
val JsonNode.description: String get() = this["description"].textValue()

val JsonNode.resources: List<JsonNode> get() = this["resources"].toList()

val JsonNode.signature: Signature
    get() {
        TODO()
    }
