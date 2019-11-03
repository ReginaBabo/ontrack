package net.nemerosa.ontrack.kdsl.core

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
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
