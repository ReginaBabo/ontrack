package net.nemerosa.ontrack.kdsl.model

import kotlin.reflect.KClass

interface ProjectEntity : Entity {

    val signature: Signature

    fun setProperty(type: String, value: Any)

    /**
     * Gets a property on this entity
     */
    fun <T : Any> getProperty(kClass: KClass<T>, type: String): T?

}