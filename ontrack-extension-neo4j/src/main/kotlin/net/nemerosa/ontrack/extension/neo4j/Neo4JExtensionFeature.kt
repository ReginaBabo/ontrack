package net.nemerosa.ontrack.extension.neo4j

import net.nemerosa.ontrack.extension.support.AbstractExtensionFeature
import net.nemerosa.ontrack.model.extension.ExtensionFeatureOptions
import org.springframework.stereotype.Component

@Component
class Neo4JExtensionFeature : AbstractExtensionFeature(
        "neo4j",
        "Neo4J",
        "Neo4J support",
        ExtensionFeatureOptions.DEFAULT
)
