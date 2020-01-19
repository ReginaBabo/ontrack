package net.nemerosa.ontrack.bdd.model.worlds

import net.nemerosa.ontrack.bdd.model.support.indexOf
import net.nemerosa.ontrack.dsl.Branch
import net.nemerosa.ontrack.dsl.Build
import net.nemerosa.ontrack.dsl.Project
import net.nemerosa.ontrack.dsl.admin.AccountGroup
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