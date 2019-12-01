package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.PropertyService
import net.nemerosa.ontrack.model.structure.PropertyType
import net.nemerosa.ontrack.ui.graphql.dsl.support.typedDataFetcher
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

abstract class PropertyTypeFieldContributor<T : Any, P : PropertyType<T>>(
        private val propertyService: PropertyService,
        private val propertyType: KClass<P>
) : TypeFieldContributor {

    override fun isClassSupported(type: KClass<*>): Boolean =
            type.isSubclassOf(ProjectEntity::class)

    override fun addFields(builder: TypeRuntimeWiring.Builder) {
        builder.typedDataFetcher<ProjectEntity, T>("messageProperty") { entity ->
            propertyService.getProperty(entity, propertyType.java).value
        }
    }
}
