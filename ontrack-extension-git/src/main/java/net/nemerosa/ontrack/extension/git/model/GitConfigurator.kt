package net.nemerosa.ontrack.extension.git.model;

import net.nemerosa.ontrack.model.structure.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Extracting the Git configuration from a project.
 */
public interface GitConfigurator {

    Optional<GitConfiguration> getConfiguration(Project project);

    /**
     * Converts a PR key to an ID when possible
     */
    @Nullable
    Integer toPullRequestID(@NotNull String key);

    /**
     * Loads a pull request
     *
     * @param configuration Configuration to use
     * @param id            ID of the pull request
     * @return Pull request or null if not existing
     */
    @Nullable
    GitPullRequest getPullRequest(@NotNull GitConfiguration configuration, int id);
}
