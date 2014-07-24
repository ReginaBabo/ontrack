package net.nemerosa.ontrack.extension.git.client.impl;

import net.nemerosa.ontrack.extension.git.client.GitClient;
import net.nemerosa.ontrack.extension.git.client.GitClientFactory;
import net.nemerosa.ontrack.extension.git.model.GitConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultGitClientFactory implements GitClientFactory {

    private final GitRepositoryManager repositoryManager;

    @Autowired
    public DefaultGitClientFactory(GitRepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Override
    public GitClient getClient(GitConfiguration gitConfiguration) {
        // Repository
        GitRepository repository = repositoryManager.getRepository(
                gitConfiguration.getRemote(),
                gitConfiguration.getBranch());
        // Client
        return new DefaultGitClient(repository, gitConfiguration);
    }

}
