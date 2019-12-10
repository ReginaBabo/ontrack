package net.nemerosa.ontrack.extension.git.model

class GitPullRequest (
        val id: Int,
        val key: String,
        val source: String,
        val target: String,
        val title: String
)