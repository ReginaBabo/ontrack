package net.nemerosa.ontrack.extension.issues.support

import net.nemerosa.ontrack.extension.issues.export.IssueExportServiceFactory
import net.nemerosa.ontrack.extension.issues.model.Issue
import net.nemerosa.ontrack.extension.issues.model.IssueServiceConfiguration
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.support.MessageAnnotation
import net.nemerosa.ontrack.model.support.MessageAnnotator
import net.nemerosa.ontrack.model.support.RegexMessageAnnotator
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.util.*

@Component
class MockIssueServiceExtension(
        extensionFeature: MockIssueServiceFeature,
        issueExportServiceFactory: IssueExportServiceFactory
) : AbstractIssueServiceExtension
(
        extensionFeature, "mock", "Mock issue", issueExportServiceFactory
) {

    companion object {
        protected fun getIssueId(issueKey: String): Int {
            return Integer.parseInt(StringUtils.stripStart(issueKey, "#"), 10)
        }
    }

    private val issueRegex = "#(\\d+)".toRegex()

    /**
     * Issues registered for testing
     */
    private val issues = mutableMapOf<Int, MockIssue>()

    /**
     * Resets the list of registered issues
     */
    fun resetIssues() {
        issues.clear()
    }

    /**
     * Registers some issues
     */
    fun register(vararg issues: MockIssue) {
        issues.forEach { this.issues[it.key.toInt()] = it }
    }

    override fun getIssueTypes(issueServiceConfiguration: IssueServiceConfiguration, issue: Issue?): Set<String> {
        return if (issue is MockIssue) {
            setOf(issue.type)
        } else {
            emptySet()
        }
    }

    override fun getConfigurationList(): List<IssueServiceConfiguration> {
        return listOf(MockIssueServiceConfiguration.INSTANCE)
    }

    override fun getConfigurationByName(name: String): IssueServiceConfiguration? {
        return if (name == MockIssueServiceConfiguration.INSTANCE.name) {
            MockIssueServiceConfiguration.INSTANCE
        } else {
            null
        }
    }

    override fun validIssueToken(token: String?): Boolean {
        return token != null && issueRegex.matchEntire(token) != null
    }

    override fun extractIssueKeysFromMessage(issueServiceConfiguration: IssueServiceConfiguration, message: String?): Set<String> {
        val result = mutableSetOf<String>()
        if (message != null) {
            val matches = issueRegex.findAll(message)
            for (match in matches) {
                val issueKey = match.groupValues[1]
                result += issueKey
            }
        }
        return result
    }

    override fun getIssueId(issueServiceConfiguration: IssueServiceConfiguration, token: String): Optional<String> {
        return if (StringUtils.isNumeric(token) || validIssueToken(token)) {
            Optional.of(getIssueId(token).toString())
        } else {
            Optional.empty()
        }
    }

    override fun getMessageAnnotator(issueServiceConfiguration: IssueServiceConfiguration?): Optional<MessageAnnotator> {
        return Optional.of(
                RegexMessageAnnotator(issueRegex.pattern) { token: String ->
                    MessageAnnotation.of("a")
                            .attr("href", "http://issue/${token.substring(1)}")
                            .text(token)
                }
        )
    }

    override fun getLinkForAllIssues(issueServiceConfiguration: IssueServiceConfiguration, issues: List<Issue>): String? = null

    override fun getIssue(issueServiceConfiguration: IssueServiceConfiguration, issueKey: String): Issue {
        val key = getIssueId(issueKey)
        return issues[key] ?: MockIssue(
                key,
                MockIssueStatus.OPEN,
                "bug"
        )
    }

    override fun getLinkedIssues(project: Project, issueServiceConfiguration: IssueServiceConfiguration, issue: Issue): Collection<Issue> {
        val issues = mutableMapOf<Int, MockIssue>()
        if (issue is MockIssue) {
            collectLinkedIssues(issue, issues)
        }
        return issues.values
    }

    private fun collectLinkedIssues(issue: MockIssue, issues: MutableMap<Int, MockIssue>) {
        issues[issue.key.toInt()] = issue
        issue.links.forEach { link ->
            if (!issues.containsKey(link.key.toInt())) {
                collectLinkedIssues(link, issues)
            }
        }
    }

}
