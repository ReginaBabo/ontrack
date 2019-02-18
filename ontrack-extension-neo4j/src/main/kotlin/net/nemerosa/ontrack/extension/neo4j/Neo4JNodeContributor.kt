package net.nemerosa.ontrack.extension.neo4j

import net.nemerosa.ontrack.extension.neo4j.model.NodeContext
import net.nemerosa.ontrack.model.extension.Extension

interface Neo4JNodeContributor<T> : Extension {

    operator fun invoke(nodeContext: NodeContext<T>)

    fun appliesFor(type: Class<*>): Boolean
}