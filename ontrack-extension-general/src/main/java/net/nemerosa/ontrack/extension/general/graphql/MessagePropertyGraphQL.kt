package net.nemerosa.ontrack.extension.general.graphql

import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.extension.general.MessageProperty
import net.nemerosa.ontrack.extension.general.MessagePropertyType
import net.nemerosa.ontrack.extension.general.MessageType
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.PropertyService
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.core.ProjectEntityInput
import net.nemerosa.ontrack.ui.graphql.dsl.root.mutation.AbstractTypedRootMutationGraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.parse
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

/**
 * Setting a message property
 */
@Component
class MutationMessageProperty(
        private val structureService: StructureService,
        private val propertyService: PropertyService
) : AbstractTypedRootMutationGraphQLContributor<MutationMessagePropertyInput, ProjectEntity>() {

    override val name: String = "messageProperty"

    override fun parseInput(env: DataFetchingEnvironment): MutationMessagePropertyInput = env.parse()

    override fun getValue(input: MutationMessagePropertyInput): ProjectEntity {
        val entity = input.entity.loadProjectEntity(structureService)
        val value = MessageProperty(input.type, input.text)
        propertyService.editProperty(
                entity,
                MessagePropertyType::class.java,
                value
        )
        return entity
    }

}

data class MutationMessagePropertyInput(
        val entity: ProjectEntityInput,
        val type: MessageType = MessageType.INFO,
        val text: String
)
