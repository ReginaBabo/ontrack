package net.nemerosa.ontrack.kdsl.core.support

import com.fasterxml.jackson.databind.JsonNode
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun jsonText(json: JsonNode): JsonProperty<String> = JsonProperty(json) { node, field ->
    node.getText(field)
}

fun jsonOptionalText(json: JsonNode): JsonProperty<String?> = JsonProperty(json) { node, field ->
    node.getOptionalText(field)
}

fun <T> jsonParser(json: JsonNode, parser: (JsonNode) -> T): JsonProperty<T> = JsonProperty(json) { node, field ->
    parser(node.get(field))
}

inline fun <reified T> jsonObject(json: JsonNode) = jsonParser(json) {
    it.parse<T>()
}

class JsonProperty<T>(
        private val json: JsonNode,
        private val mapper: (node: JsonNode, field: String) -> T
) : ReadOnlyProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T =
            mapper(json, property.name)
}
