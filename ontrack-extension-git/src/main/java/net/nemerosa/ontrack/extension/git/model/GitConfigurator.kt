package net.nemerosa.ontrack.extension.git.model

import net.nemerosa.ontrack.model.structure.Project

/**
 * Extracting the Git configuration from a project.
 */
interface GitConfigurator {

    /**
     * Gets the Git configuration for a project
     */
    fun getConfiguration(project: Project): GitConfiguration?

    /**
     * Converts a PR key to an ID when possible
     */
    fun toPullRequestID(key: String): Int?

    /**
     * Loads a pull request
     *
     * @param configuration Configuration to use
     * @param id            ID of the pull request
     * @return Pull request or null if not existing
     */
    fun getPullRequest(configuration: GitConfiguration, id: Int): GitPullRequest?
}