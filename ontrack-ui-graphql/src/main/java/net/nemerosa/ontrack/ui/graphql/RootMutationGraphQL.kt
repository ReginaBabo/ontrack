package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.support.get
import net.nemerosa.ontrack.ui.graphql.dsl.support.input
import org.springframework.stereotype.Component

@Component
class RootMutationGraphQL(
        private val structureService: StructureService,
        private val securityService: SecurityService
) : AbstractGraphQLContributor() {
    override fun wire(wiring: RuntimeWiring.Builder) {
        val mutationType = TypeRuntimeWiring.newTypeWiring("Mutation")
                .dataFetcher("createProject") { env ->
                    val input = env.input("project").let {
                        CreateProjectInput(
                                it[CreateProjectInput::name],
                                it[CreateProjectInput::description],
                                it[CreateProjectInput::disabled]
                        )
                    }
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

        wiring.type(mutationType)
    }
}

data class CreateProjectInput(
        val name: String,
        val description: String?,
        val disabled: Boolean?
)
