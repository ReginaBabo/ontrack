package net.nemerosa.ontrack.extension.neo4j

import net.nemerosa.ontrack.extension.support.AbstractExtension
import net.nemerosa.ontrack.model.extension.ExtensionFeature

abstract class AbstractNeo4JNodeContributor<T>(
        extensionFeature: ExtensionFeature,
        private val type: Class<T>
) : AbstractExtension(extensionFeature), Neo4JNodeContributor<T> {

    override fun appliesFor(type: Class<*>): Boolean {
        return type.isAssignableFrom(this.type)
    }
}