package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.Property
import net.nemerosa.ontrack.model.structure.PropertyService
import net.nemerosa.ontrack.ui.graphql.dsl.support.typedDataFetcher
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Component
class PropertiesFieldContributor(
        private val propertyService: PropertyService
) : TypeFieldContributor {

    override fun isClassSupported(type: KClass<*>): Boolean =
            type.isSubclassOf(ProjectEntity::class)

    override fun addFields(builder: TypeRuntimeWiring.Builder) {
        builder.typedDataFetcher<ProjectEntity, PropertyListInput, List<Property<*>>>("properties") { entity, input ->
            fetchProperties(entity, input)
        }
    }

    private fun fetchProperties(entity: ProjectEntity, input: PropertyListInput): List<Property<*>> {
        val (type, hasValue) = input
        // Gets the raw list
        return propertyService.getProperties(entity)
                // Filter by type
                .filter { property: Property<*> ->
                    type.isNullOrBlank() || type == property.typeDescriptor.typeName
                }
                // Filter by value
                .filter { property: Property<*> -> !hasValue || !property.isEmpty }
    }
}