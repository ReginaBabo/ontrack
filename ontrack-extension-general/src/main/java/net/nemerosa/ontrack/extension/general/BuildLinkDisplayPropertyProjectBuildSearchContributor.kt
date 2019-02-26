package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.model.structure.BuildSearchForm
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.ProjectBuildSearchContributor
import net.nemerosa.ontrack.model.structure.PropertyService
import org.springframework.stereotype.Component

/**
 * Returns a search form based on the [ReleasePropertyType] if the project has
 * a [BuildLinkDisplayPropertyType] setting the label as default name.
 */
@Component
class BuildLinkDisplayPropertyProjectBuildSearchContributor(
        private val propertyService: PropertyService
) : ProjectBuildSearchContributor {

    override val priority: Int = 10

    override fun getSearchForm(project: Project, name: String): BuildSearchForm? {
        val display: BuildLinkDisplayProperty? = propertyService.getProperty(project, BuildLinkDisplayPropertyType::class.java).value
        return if (display != null && display.useLabel) {
            BuildSearchForm()
                    .withProperty(ReleasePropertyType::class.java.name)
                    .withPropertyValue(name)
        } else {
            null
        }
    }

}