package net.nemerosa.ontrack.extension.neo4j

import net.nemerosa.ontrack.extension.neo4j.model.NodeContext
import net.nemerosa.ontrack.model.extension.ExtensionFeature
import org.springframework.stereotype.Component

@Component
class NOPNeo4JNodeContributor(
        private val neo4JExtensionFeature: Neo4JExtensionFeature
) : Neo4JNodeContributor<Any> {

    override fun getFeature(): ExtensionFeature = neo4JExtensionFeature

    override fun appliesFor(type: Class<*>): Boolean = false

    override fun invoke(nodeContext: NodeContext<Any>) {}
}