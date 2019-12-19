package net.nemerosa.ontrack.extension.git.mocking

import net.nemerosa.ontrack.extension.git.model.GitConfiguration
import net.nemerosa.ontrack.extension.issues.model.ConfiguredIssueService
import net.nemerosa.ontrack.model.support.UserPassword
import java.util.*

class GitMockingConfiguration: GitConfiguration {

    override fun getConfiguredIssueService(): Optional<ConfiguredIssueService> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRemote(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCredentials(): Optional<UserPassword> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFileAtCommitLink(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommitLink(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIndexationInterval(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}