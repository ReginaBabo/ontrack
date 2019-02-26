package net.nemerosa.ontrack.model.structure

/**
 * Contributes to a project search based on a token.
 */
interface ProjectBuildSearchContributor {

    /**
     * Priority of the search (0 - default, higher for highest level of precedence)
     */
    val priority: Int

    /**
     * Gets a search form on the build name for the given project.
     */
    fun getSearchForm(project: Project, name: String): BuildSearchForm?

}

