package net.nemerosa.ontrack.kdsl.core

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

val mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

inline fun <reified T> JsonNode.parse(): T =
        mapper.treeToValue(this, T::class.java)
