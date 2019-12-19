package net.nemerosa.ontrack.extension.github.client;

import net.nemerosa.ontrack.extension.git.model.GitPullRequest;
import net.nemerosa.ontrack.extension.github.model.GitHubIssue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Client used to connect to a GitHub engine from Ontrack.
 */
public interface OntrackGitHubClient {

    /**
     * Gets an issue from a repository.
     *
     * @param repository Repository name, like <code>nemerosa/ontrack</code>
     * @param id         ID of the issue
     * @return Details about the issue
     */
    GitHubIssue getIssue(String repository, int id);

    /**
     * Gets the list of repositories available using this client.
     */
    List<String> getRepositories();

    /**
     * Gets a pull request using its ID
     *
     * @param repository Repository name, like <code>nemerosa/ontrack</code>
     * @param id         ID of the pull request
     * @return Details of the pull request
     */
    @Nullable
    GitPullRequest getPullRequest(@NotNull String repository, int id);
}
