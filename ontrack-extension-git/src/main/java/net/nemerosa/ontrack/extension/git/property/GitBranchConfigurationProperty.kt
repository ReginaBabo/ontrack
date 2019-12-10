package net.nemerosa.ontrack.extension.git.property

import net.nemerosa.ontrack.model.structure.ServiceConfiguration

class GitBranchConfigurationProperty(

        /**
         * Git branch
         */
        val branch: String,

        /**
         * Git pull request key
         */
        val pullRequest: String?,

        /**
         * Build link
         */
        val buildCommitLink: ServiceConfiguration?,

        /**
         * Build overriding policy when synchronizing
         */
        val isOverride: Boolean,

        /**
         * Interval in minutes for build/tag synchronization
         */
        val buildTagInterval: Int

)
