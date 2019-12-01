package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.ui.graphql.dsl.root.mutation.RootMutationGraphQLContributor
import org.springframework.stereotype.Component

@Component
class RootMutationGraphQL(
        private val rootMutationGraphQLContributors: List<RootMutationGraphQLContributor>
) : AbstractGraphQLContributor() {
    override fun wire(wiring: RuntimeWiring.Builder) {
        val mutationType = TypeRuntimeWiring.newTypeWiring("Mutation")
        val finalType = rootMutationGraphQLContributors.fold(mutationType) { r, t ->
            t.contribute(r)
        }
        wiring.type(finalType)
    }
}
