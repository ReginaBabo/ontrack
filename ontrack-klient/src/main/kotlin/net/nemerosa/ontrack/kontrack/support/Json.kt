package net.nemerosa.ontrack.kontrack.support

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.json.JsonUtils

inline fun <reified T> JsonNode.parse(): T {
    return JsonUtils.parse(this, T::class.java)
}
