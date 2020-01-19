package net.nemerosa.ontrack.dsl

/**
 * Gets a property on this entity
 */
inline fun <reified T : Any> ProjectEntity.property(type: String): T? = getProperty(T::class, type)
