package net.nemerosa.ontrack.extension.general.graphql

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.extension.general.MessageProperty
import net.nemerosa.ontrack.extension.general.MessagePropertyType
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.PropertyService
import net.nemerosa.ontrack.ui.graphql.dsl.support.typedDataFetcher
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Message property on all project entities
 */
@Component
class MessagePropertyTypeFieldContributor(
        private val propertyService: PropertyService
) : TypeFieldContributor {

    override fun isClassSupported(type: KClass<*>): Boolean =
            type.isSubclassOf(ProjectEntity::class)

    override fun addFields(builder: TypeRuntimeWiring.Builder) {
        builder.typedDataFetcher<ProjectEntity, MessageProperty>("messageProperty") { entity ->
            propertyService.getProperty(entity, MessagePropertyType::class.java).value
        }
    }

}