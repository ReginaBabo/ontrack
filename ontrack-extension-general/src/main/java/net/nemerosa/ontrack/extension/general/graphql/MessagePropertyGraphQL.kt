package net.nemerosa.ontrack.extension.general.graphql

import net.nemerosa.ontrack.extension.general.MessageProperty
import net.nemerosa.ontrack.extension.general.MessagePropertyType
import net.nemerosa.ontrack.model.structure.PropertyService
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.core.PropertyMutation
import net.nemerosa.ontrack.ui.graphql.core.PropertyTypeFieldContributor
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
        structureService: StructureService,
        propertyService: PropertyService
) : PropertyMutation<MessageProperty, MessagePropertyType>(
        "messageProperty",
        MessagePropertyType::class,
        structureService,
        propertyService
)
