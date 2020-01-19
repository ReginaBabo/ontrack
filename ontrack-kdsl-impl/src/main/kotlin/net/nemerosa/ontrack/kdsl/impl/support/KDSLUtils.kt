package net.nemerosa.ontrack.kdsl.impl.support

fun query(vararg pairs: Pair<String, Any?>): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    pairs.forEach { (name, value) ->
        if (value != null) {
            map[name] = value
        }
    }
    return map.toMap()
}