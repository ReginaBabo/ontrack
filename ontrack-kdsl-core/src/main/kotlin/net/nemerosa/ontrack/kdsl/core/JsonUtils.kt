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