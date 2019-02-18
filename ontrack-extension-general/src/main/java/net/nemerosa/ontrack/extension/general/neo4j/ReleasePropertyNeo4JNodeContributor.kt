package net.nemerosa.ontrack.extension.general.neo4j

import net.nemerosa.ontrack.extension.general.GeneralExtensionFeature
import net.nemerosa.ontrack.extension.general.ReleasePropertyType
import net.nemerosa.ontrack.extension.neo4j.AbstractNeo4JNodeContributor
import net.nemerosa.ontrack.extension.neo4j.model.NodeContext
import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.PropertyService
import org.springframework.stereotype.Component

@Component
class ReleasePropertyNeo4JNodeContributor(
        extensionFeature: GeneralExtensionFeature,
        private val propertyService: PropertyService
) : AbstractNeo4JNodeContributor<Build>(
        extensionFeature,
        Build::class.java
) {
    override fun invoke(nodeContext: NodeContext<Build>) {
        nodeContext.column("label" to { build ->
            propertyService.getProperty(build, ReleasePropertyType::class.java).value?.name ?: ""
        })
        nodeContext.column("name" to { build ->
            propertyService.getProperty(build, ReleasePropertyType::class.java).value?.name ?: build.name
        })
    }
}