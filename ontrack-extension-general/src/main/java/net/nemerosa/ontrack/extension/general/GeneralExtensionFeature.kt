package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.extension.neo4j.Neo4JExtensionFeature
import net.nemerosa.ontrack.extension.support.AbstractExtensionFeature
import net.nemerosa.ontrack.model.extension.ExtensionFeatureOptions
import org.springframework.stereotype.Component

@Component
class GeneralExtensionFeature(neo4JExtensionFeature: Neo4JExtensionFeature) : AbstractExtensionFeature(
        "general",
        "General",
        "Core extensions",
        ExtensionFeatureOptions.DEFAULT
                .withGui(true)
                .withDependency(neo4JExtensionFeature)
)
