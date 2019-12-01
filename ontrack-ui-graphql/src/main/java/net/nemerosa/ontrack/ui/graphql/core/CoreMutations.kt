package net.nemerosa.ontrack.ui.graphql.core

import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.root.mutation.RootMutationDelegates.rootMutation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreMutations(
        private val structureService: StructureService,
        private val securityService: SecurityService
) {

    @get:Bean
    val createProject by rootMutation("project" to CreateProjectInput::class) { input ->
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

}

data class CreateProjectInput(
        val name: String,
        val description: String?,
        val disabled: Boolean?
)
