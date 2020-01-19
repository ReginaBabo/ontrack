package net.nemerosa.ontrack.kdsl.model

/**
 * Gets a property on this entity
 */
inline fun <reified T : Any> ProjectEntity.property(type: String): T? = getProperty(T::class, type)
