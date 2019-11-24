package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.security.AccountService
import org.springframework.stereotype.Component

@Component
class RootQueryGraphQL(
        private val accountService: AccountService
) : AbstractGraphQLContributor() {
    override fun wire(wiring: RuntimeWiring.Builder) {
        wiring.type(
                TypeRuntimeWiring.newTypeWiring("Query")
                        // TODO Use contributors
                        .dataFetcher("accountGroups") { env ->
                            val id: Int? = env.getArgument("id")
                            val name: String? = env.getArgument("name")
                            val mapping: String? = env.getArgument("mapping")
                            // TODO Filter
                            accountService.accountGroups
                        }
        )
    }
}