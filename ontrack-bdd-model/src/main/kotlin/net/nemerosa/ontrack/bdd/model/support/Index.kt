package net.nemerosa.ontrack.bdd.model.support

class Index<T> {

    private val index = mutableMapOf<String, T>()

    operator fun get(name: String): T =
            index[name] ?: throw IllegalStateException("Cannot find registered entry with name: $name")

    operator fun set(name: String, value: T) {
        index[name] = value
    }

    fun clear() {
        index.clear()
    }

    fun getOrNull(name: String): T? = index[name]

}

fun <T> indexOf() = Index<T>()
