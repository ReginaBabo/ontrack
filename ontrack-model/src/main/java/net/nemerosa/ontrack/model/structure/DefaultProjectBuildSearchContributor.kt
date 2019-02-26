package net.nemerosa.ontrack.model.structure

import org.springframework.stereotype.Component

/**
 * Search based on the exact build name.
 */
@Component
class DefaultProjectBuildSearchContributor : ProjectBuildSearchContributor {

    /**
     * Lowest level of priority
     */
    override val priority: Int = 0

    override fun getSearchForm(project: Project, name: String): BuildSearchForm? =
            getDefaultSearchForm(name)

    companion object {
        fun getDefaultSearchForm(name: String): BuildSearchForm {
            return BuildSearchForm().withBuildName(name).withBuildExactMatch(true)
        }
    }

}