package net.nemerosa.ontrack.bdd.model.support

fun Map<String, String>.getInt(key: String): Int? = get(key)?.toInt(10)

fun Map<String, String>.getListOfStrings(key: String): List<String>? =
        get(key)?.split(",")?.map { it.trim() }
