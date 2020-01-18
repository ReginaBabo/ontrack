package net.nemerosa.ontrack.dsl

fun toMapNotNull(vararg pairs: Pair<String, Any?>): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    pairs.forEach { (name, value) ->
        if (value != null) {
            map[name] = value
        }
    }
    return map.toMap()
}