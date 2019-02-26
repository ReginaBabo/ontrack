package net.nemerosa.ontrack.model.structure

object ProjectBuildSearchContributorUtils {

    fun getSearchForm(project: Project, name: String, contributors: List<ProjectBuildSearchContributor>): BuildSearchForm {
        return contributors
                .sortedByDescending { it.priority }
                .mapNotNull { it.getSearchForm(project, name) }
                .firstOrNull()
                ?: DefaultProjectBuildSearchContributor.getDefaultSearchForm(name)
    }

}