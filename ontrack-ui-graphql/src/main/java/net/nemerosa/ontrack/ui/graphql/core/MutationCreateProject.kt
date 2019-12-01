package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.root.mutation.AbstractTypedRootMutationGraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.get
import org.springframework.stereotype.Component

@Component
class MutationCreateProject(
        private val structureService: StructureService,
        private val securityService: SecurityService
) : AbstractTypedRootMutationGraphQLContributor<CreateProjectInput, Project>() {

    override val name: String = "createProject"

    override fun parseInput(env: DataFetchingEnvironment): CreateProjectInput = env["project"]

    override fun getValue(input: CreateProjectInput): Project =
            structureService.newProject(
                    Project(
                            ID.NONE,
                            input.name,
                            input.description,
                            input.disabled ?: false,
                            securityService.currentSignature
                    )
            )

}

data class CreateProjectInput(
        val name: String,
        val description: String?,
        val disabled: Boolean?
)
