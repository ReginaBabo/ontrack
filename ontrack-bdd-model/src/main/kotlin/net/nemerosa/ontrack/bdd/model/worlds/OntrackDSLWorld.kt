package net.nemerosa.ontrack.bdd.model.worlds

import net.nemerosa.ontrack.bdd.model.support.indexOf
import net.nemerosa.ontrack.kdsl.model.AccountGroup
import net.nemerosa.ontrack.kdsl.model.Build
import net.nemerosa.ontrack.kdsl.model.Project
import org.springframework.stereotype.Component

@Component
class OntrackDSLWorld {

    /**
     * Register of projects
     */
    val projects = indexOf<Project>()

    /**
     * Register of builds
     */
    val builds = indexOf<Build>()

    /**
     * Register of accounts
     */
    val accounts = indexOf<AccountWithPassword>()

    /**
     * Register of account groups
     */
    val accountGroups = indexOf<AccountGroup>()

    /**
     * Clears all context
     */
    fun clear() {
        projects.clear()
        builds.clear()
        accounts.clear()
        accountGroups.clear()
    }

}