package net.nemerosa.ontrack.ui.graphql.core

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
