package net.nemerosa.ontrack.extension.issues.support

import net.nemerosa.ontrack.extension.issues.model.Issue
import net.nemerosa.ontrack.extension.issues.model.IssueStatus

import java.time.LocalDateTime

class MockIssue
@JvmOverloads
constructor(
        private val key: Int,
        private val status: MockIssueStatus,
        val type: String,
        val links: MutableCollection<MockIssue> = mutableListOf()
) : Issue {

    override fun getKey(): String = key.toString()

    override fun getSummary(): String = "Issue #$key"

    override fun getUrl(): String = "uri:issue/$key"

    override fun getStatus(): IssueStatus = status

    override fun getDisplayKey(): String = "#$key"

    override fun getUpdateTime(): LocalDateTime =
            LocalDateTime.of(2014, 12, 10, 8, 32, key % 60)

    fun withLinks(issues: Collection<MockIssue>): MockIssue {
        links.clear()
        links.addAll(issues)
        return this
    }

}
