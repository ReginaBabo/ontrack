package net.nemerosa.ontrack.bdd.binding.steps.worlds

import net.nemerosa.ontrack.kdsl.model.AccountGroup
import net.nemerosa.ontrack.kdsl.model.Project
import org.springframework.stereotype.Component

@Component
class OntrackDSLWorld {

    /**
     * Register of projects
     */
    val projects = mutableMapOf<String, Project>()

    /**
     * Register of account groups
     */
    val accountGroups = mutableMapOf<String, AccountGroup>()

    /**
     * Gets a registered project
     */
    fun getProject(projectRegisterName: String): Project =
            projects[projectRegisterName]
                    ?: throw IllegalStateException("Cannot find project registered with name $projectRegisterName")

    /**
     * Gets a registered account group
     */
    fun getAccountGroup(accountGroupRegisterName: String): AccountGroup =
            accountGroups[accountGroupRegisterName]
                    ?: throw IllegalStateException("Cannot find account group registered with name $accountGroupRegisterName")

}