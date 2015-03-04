package net.nemerosa.ontrack.extension.git.service

import net.nemerosa.ontrack.common.Time
import net.nemerosa.ontrack.extension.git.model.BasicGitConfiguration
import net.nemerosa.ontrack.extension.git.model.ConfiguredBuildGitCommitLink
import net.nemerosa.ontrack.extension.git.model.GitBranchConfiguration
import net.nemerosa.ontrack.extension.git.property.GitBranchConfigurationProperty
import net.nemerosa.ontrack.extension.git.property.GitBranchConfigurationPropertyType
import net.nemerosa.ontrack.extension.git.support.TagPattern
import net.nemerosa.ontrack.extension.git.support.TagPatternBuildNameGitCommitLink
import net.nemerosa.ontrack.extension.issues.IssueServiceRegistry
import net.nemerosa.ontrack.git.GitRepositoryClient
import net.nemerosa.ontrack.git.GitRepositoryClientFactory
import net.nemerosa.ontrack.git.model.GitTag
import net.nemerosa.ontrack.model.job.JobQueueService
import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.model.support.ApplicationLogService
import net.nemerosa.ontrack.tx.TransactionService
import org.junit.Before
import org.junit.Test

import static net.nemerosa.ontrack.model.structure.NameDescription.nd
import static org.mockito.Mockito.*

/**
 * Testing the sync between builds and Git tags.
 *
 * @see GitService#launchBuildSync(net.nemerosa.ontrack.model.structure.ID)
 */
class GitBuildSyncIT {

    private GitServiceImpl gitService
    private StructureService structureService
    private PropertyService propertyService
    private GitRepositoryClientFactory gitClientFactory
    private GitRepositoryClient gitClient

    /**
     * Service
     */
    @Before
    void 'Git service'() {
        gitClientFactory = mock(GitRepositoryClientFactory)
        gitClient = mock(GitRepositoryClient)

        structureService = mock(StructureService)
        propertyService = mock(PropertyService)

        def securityService = mock(SecurityService)
        when(securityService.getCurrentSignature()).thenReturn(Signature.of('user'))

        gitService = new GitServiceImpl(
                structureService,
                propertyService,
                mock(IssueServiceRegistry),
                mock(JobQueueService),
                securityService,
                mock(TransactionService),
                mock(ApplicationLogService),
                gitClientFactory,
                mock(BuildGitCommitLinkService),
                [], scmService
        )
    }

    @Test
    void 'Master sync'() {
        Project project = Project.of(nd('P', "Project")).withId(ID.of(1))
        Branch branch = Branch.of(project, nd('1.2', "Branch 1.2")).withId(ID.of(2))

        BuildGitCommitLinkService buildGitCommitLinkService = mock(BuildGitCommitLinkService)

        when(structureService.findBuildByName(eq('P'), eq('1.2'), anyString())).thenReturn(Optional.empty())

        def configuredBuildGitCommitLink = new ConfiguredBuildGitCommitLink<>(
                new TagPatternBuildNameGitCommitLink(),
                new TagPattern("1.2.*"))
        when(propertyService.getProperty(branch, GitBranchConfigurationPropertyType)).thenReturn(
                Property.of(
                        new GitBranchConfigurationPropertyType(buildGitCommitLinkService),
                        new GitBranchConfigurationProperty(
                                'master',
                                configuredBuildGitCommitLink.toServiceConfiguration(),
                                true,
                                0
                        )
                )
        )

        def gitConfiguration = BasicGitConfiguration.empty()
        def gitBranchConfiguration = new GitBranchConfiguration(
                gitConfiguration,
                'master',
                configuredBuildGitCommitLink,
                false,
                0
        )
        when(gitClientFactory.getClient(gitConfiguration.gitRepository)).thenReturn(gitClient)

        when(gitClient.getTags()).thenReturn([
                new GitTag('1.1.6', Time.now()),
                new GitTag('1.1.7', Time.now()),
                new GitTag('1.2.0', Time.now()),
                new GitTag('1.2.1', Time.now()),
                new GitTag('1.2.2', Time.now()),
        ])

        gitService.buildSync(
                branch,
                gitBranchConfiguration,
                { message -> println message }
        )

        ['1.2.0', '1.2.1', '1.2.2'].each { String tagName ->
            verify(structureService, times(3)).newBuild(
                    Build.of(
                            branch,
                            nd(
                                    tagName,
                                    "Imported from Git tag ${tagName}"
                            ),
                            any(Signature)
                    )
            );
        }
    }

}
