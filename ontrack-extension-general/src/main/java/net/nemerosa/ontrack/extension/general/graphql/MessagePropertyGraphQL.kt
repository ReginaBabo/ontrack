package net.nemerosa.ontrack.extension.general.graphql

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.extension.general.MessageProperty
import net.nemerosa.ontrack.extension.general.MessagePropertyType
import net.nemerosa.ontrack.extension.general.MessageType
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.PropertyService
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.core.ProjectEntityInput
import net.nemerosa.ontrack.ui.graphql.core.PropertyTypeFieldContributor
import net.nemerosa.ontrack.ui.graphql.dsl.root.mutation.AbstractTypedRootMutationGraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.parse
import org.springframework.stereotype.Component

/**
 * Message property on all project entities
 */
@Component
class MessagePropertyTypeFieldContributor(
        propertyService: PropertyService
) : PropertyTypeFieldContributor<MessageProperty, MessagePropertyType>(
        propertyService,
        MessagePropertyType::class
)

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
