package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.nemerosa.ontrack.bdd.model.worlds.withPassword
import net.nemerosa.ontrack.kdsl.model.*
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackDSLSteps : AbstractOntrackDSL() {

    @Autowired
    private lateinit var ontrackDSLWorld: OntrackDSLWorld

    @Autowired
    private lateinit var ontrackUtilityWorld: OntrackUtilityWorld

    @Step
    fun createAndRegisterProject(projectRef: String) {
        // Actual name of the project
        val projectName = ontrackUtilityWorld.uniqueName("project", projectRef)
        // Gets or creates the project, registers it and returns it
        ontrack.project(projectName).apply {
            ontrackDSLWorld.projects[projectRef] = this
            ontrackUtilityWorld.id(projectRef, id)
        }
    }

    @Step
    fun createAndRegisterBuild(buildRef: String) {
        val projectName = ontrackUtilityWorld.uniqueName("project", buildRef)
        val branchName = ontrackUtilityWorld.uniqueName("branch", buildRef)
        val buildName = ontrackUtilityWorld.uniqueName("build", buildRef)
        ontrack.project(projectName) {
            branch(branchName) {
                build(buildName) {
                    ontrackDSLWorld.builds[buildRef] = this
                    ontrackUtilityWorld.id(buildRef, id)
                }
            }
        }
    }

    @Step
    fun createBranchInProject(branchName: String, projectRegisterName: String) {
        // Gets the project
        val project = ontrackDSLWorld.projects[projectRegisterName]
        // Creates a branch in this project
        project.branch(branchName)
    }

    @Step
    fun createValidationStampInBranchAndProject(validationStampName: String, branchName: String, projectRegisterName: String) {
        // Gets the project
        val project = ontrackDSLWorld.projects[projectRegisterName]
        // Creates/gets the branch in this project and creates a validation stamp
        project.branch(branchName) {
            validationStamp(validationStampName)
        }
    }

    @Step
    fun createAndRegisterAccountGroup(accountGroupRegisterName: String) {
        // Actual name of the account group
        val accountGroupName = ontrackUtilityWorld.uniqueName("accountGroup", "AG")
        // Gets or creates the account group, registers it and returns it
        ontrack.accounts.accountGroup(accountGroupName).apply {
            ontrackDSLWorld.accountGroups[accountGroupRegisterName] = this
        }
    }

    @Step
    fun setAccountGroupGlobalPermission(accountGroupRegisterName: String, role: String) {
        val group = ontrackDSLWorld.accountGroups[accountGroupRegisterName]
        group.setGlobalPermission(role)
    }

    @Step
    fun createAndRegisterAccountInGroup(accountRegisterName: String, accountGroupRegisterName: String) {
        // Gets the group
        val group = ontrackDSLWorld.accountGroups[accountGroupRegisterName]
        // Password
        val password = ontrackUtilityWorld.uniqueName("password", "P")
        // Creates the account
        ontrack.accounts.createAccount(
                ontrackUtilityWorld.uniqueName("account", "A"),
                "$accountRegisterName Test",
                "$accountRegisterName@test.com",
                password,
                listOf(group.name)
        ).apply {
            ontrackDSLWorld.accounts[accountRegisterName] = this withPassword password
        }
    }

    @Step
    fun createAndRegisterAccountWithPassword(accountRegisterName: String, password: String) {
        // Creates the account
        ontrack.accounts.createAccount(
                ontrackUtilityWorld.uniqueName("account", "A"),
                "$accountRegisterName Test",
                "$accountRegisterName@test.com",
                password,
                emptyList()
        ).apply {
            ontrackDSLWorld.accounts[accountRegisterName] = this withPassword password
        }
    }
}
