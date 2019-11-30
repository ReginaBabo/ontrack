package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import org.springframework.stereotype.Component

@Component
class RootQueryGraphQL(
        private val rootQueryGraphQLContributors: List<RootQueryGraphQLContributor>
) : AbstractGraphQLContributor() {
    override fun wire(wiring: RuntimeWiring.Builder) {
        val queryType = TypeRuntimeWiring.newTypeWiring("Query")
        val finalType = rootQueryGraphQLContributors.fold(queryType) { r, t ->
            t.contribute(r)
        }
        wiring.type(finalType)
    }
}