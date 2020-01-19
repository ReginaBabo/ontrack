package net.nemerosa.ontrack.kdsl.core

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlin.reflect.KClass

val mapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JavaTimeModule())

inline fun <reified T> JsonNode.parse(): T =
        mapper.treeToValue(this, T::class.java)

fun <T : Any> JsonNode?.parseInto(kClass: KClass<T>): T? =
        if (this == null) {
            null
        } else {
            mapper.treeToValue(this, kClass.java)
        }

fun Any.toJson(): JsonNode = mapper.valueToTree(this)

fun JsonNode.deepPath(path: String): JsonNode {
    val index = path.indexOf(".")
    return if (index >= 0) {
        val prefix = path.substring(0, index)
        val suffix = path.substring(index + 1)
        val child = path(prefix)
        child.deepPath(suffix)
    } else {
        path(path)
    }
}

fun ObjectNode.removeDeepPath(path: String) {
    val index = path.indexOf(".")
    if (index >= 0) {
        val prefix = path.substring(0, index)
        val suffix = path.substring(index + 1)
        val child = path(prefix)
        if (child is ObjectNode) {
            child.removeDeepPath(suffix)
        }
    } else {
        remove(path)
    }
}

fun JsonNode.getOptionalText(name: String): String? = if (hasNonNull(name)) {
    get(name).textValue()
} else {
    null
}

fun JsonNode.getText(name: String): String = get(name).textValue()

fun JsonNode.getInt(name: String): Int = get("id").intValue()

val JsonNode.id: Int get() = getInt("id")

val JsonNode.name: String get() = this["name"].textValue()
val JsonNode.description: String get() = this["description"].textValue()

val JsonNode.resources: List<JsonNode> get() = this["resources"].toList()
