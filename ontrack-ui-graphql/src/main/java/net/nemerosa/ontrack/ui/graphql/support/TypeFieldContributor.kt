package net.nemerosa.ontrack.ui.graphql.support

import graphql.schema.idl.TypeRuntimeWiring
import kotlin.reflect.KClass

interface TypeFieldContributor {

    /**
     * Supported class
     */
    fun isClassSupported(type: KClass<*>): Boolean

    /**
     * Contributions
     */
    fun addFields(builder: TypeRuntimeWiring.Builder)

}