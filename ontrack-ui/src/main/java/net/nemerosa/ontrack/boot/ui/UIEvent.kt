package net.nemerosa.ontrack.boot.ui

import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.ProjectEntityType
import net.nemerosa.ontrack.model.structure.Signature
import net.nemerosa.ontrack.model.support.NameValue

/**
 * @property data Additional data processed from the values or entities
 */
class UIEvent(
        val eventType: String,
        val template: String,
        val signature: Signature,
        val entities: Map<ProjectEntityType, ProjectEntity>,
        val ref: ProjectEntityType?,
        val values: Map<String, NameValue>,
        val data: Map<String, *>
)
