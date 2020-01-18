package net.nemerosa.ontrack.bdd.model.worlds

import net.nemerosa.ontrack.bdd.model.support.indexOf
import net.nemerosa.ontrack.kdsl.model.deprecated.AccountGroup
import net.nemerosa.ontrack.kdsl.model.deprecated.Branch
import net.nemerosa.ontrack.kdsl.model.deprecated.Build
import net.nemerosa.ontrack.kdsl.model.deprecated.Project
import org.springframework.stereotype.Component

@Component
class OntrackDSLWorld {

    /**
     * Register of projects
     */
    val projects = indexOf<Project>()

    /**
     * Register of branches
     */
    val branches = indexOf<Branch>()

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
        branches.clear()
        builds.clear()
        accounts.clear()
        accountGroups.clear()
    }

}