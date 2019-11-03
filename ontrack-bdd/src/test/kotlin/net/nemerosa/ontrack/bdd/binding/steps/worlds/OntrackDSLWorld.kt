package net.nemerosa.ontrack.bdd.binding.steps.worlds

import net.nemerosa.ontrack.kdsl.model.AccountGroup
import net.nemerosa.ontrack.kdsl.model.Project
import org.springframework.stereotype.Component

@Component
class OntrackDSLWorld {

    /**
     * Register of projects
     */
    val projects = indexOf<Project>()

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
        accounts.clear()
        accountGroups.clear()
    }

}