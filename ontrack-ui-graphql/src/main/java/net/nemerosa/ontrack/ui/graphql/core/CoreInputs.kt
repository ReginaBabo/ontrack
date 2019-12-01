package net.nemerosa.ontrack.ui.graphql.core

import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.ProjectEntityType
import net.nemerosa.ontrack.model.structure.StructureService

data class ProjectEntityInput(
        val type: ProjectEntityType,
        val id: Int
) {
    fun loadProjectEntity(structureService: StructureService): ProjectEntity =
            type.getEntityFn(structureService).apply(ID.of(id))
}

data class BranchListInput(
        val name: String?,
        val favourite: Boolean?,
        val useModel: Boolean?
)

data class RootBranchListInput(
        val id: Int?,
        val project: String?,
        val name: String?,
        val favourite: Boolean?
)

data class RootBuildListInput(
        val id: Int?,
        val project: String?,
        val branch: String?
)

data class PropertyListInput(
        val type: String?,
        val hasValue: Boolean = false
)
