package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.*
import net.nemerosa.ontrack.bdd.model.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.nemerosa.ontrack.dsl.branch
import net.nemerosa.ontrack.dsl.validationStamp
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackBrowserSteps : AbstractOntrackBrowserSteps() {

    @Autowired
    private lateinit var ontrackDSLWorld: OntrackDSLWorld

    @Autowired
    private lateinit var ontrackUtilityWorld: OntrackUtilityWorld

    @Step
    fun checkLoginIsDisplayed() {
        currentPageAt<CompletePage> {
            checkLoginIsVisible()
        }
    }

    @Step
    fun enterLogin(username: String, password: String) {
        currentPageAt<CompletePage> {
            login(username, password)
        }
    }

    @Step
    fun enterAdminLogin() {
        enterLogin(bddProperties.ontrack.username, bddProperties.ontrack.password)
    }

    @Step
    fun loginWith(username: String, password: String) {
        currentPageAt<CompletePage> {
            userMenuLogin(username, password)
        }
    }

    @Step
    fun loginWithAccount(accountRegisterName: String) {
        val account = ontrackDSLWorld.accounts[accountRegisterName]
        loginWith(account.account.name, account.password)
    }

    @Step
    fun loginAsAdmin() {
        loginWith(bddProperties.ontrack.username, bddProperties.ontrack.password)
    }

    @Step
    fun goToValidationStampPage(validationStampName: String, branchName: String, projectRegisterName: String) {
        val project = ontrackDSLWorld.projects[projectRegisterName]
        val vs = project.apply {
            branch(branchName) {
                validationStamp(validationStampName)
            }
        }
        page<ValidationStampPage> {
            open(vs.id)
        }
    }

    @Step
    fun checkBuldUpdateCommandPresentInValidationStampPage() {
        currentPageAt<ValidationStampPage> {
            checkBuldUpdateCommandPresent()
        }
    }

    @Step
    fun goToProjectPage(projectRef: String) {
        // Gets the project name as a reference
        val projectName = ontrackDSLWorld.projects.getOrNull(projectRef)?.name
                ?: ontrackUtilityWorld.replaceTokens(projectRef)
        // Loads the project by name
        val project = ontrack.findProjectByName(projectName)
                ?: throw IllegalStateException("Cannot find project $projectName referenced by $projectRef")
        page<ProjectPage> {
            open(project.id)
        }
    }

    @Step
    fun goToBranchPage(branchRef: String) {
        val branch = ontrackDSLWorld.branches[branchRef]
        page<BranchPage> {
            open(branch.id)
        }
    }

    @Step
    fun goToBuildPage(buildRef: String) {
        val build = ontrackDSLWorld.builds[buildRef]
        page<BuildPage> {
            open(build.id)
        }
    }

    @Step
    fun navigateToAPIPage() {
        currentPageAt<CompletePage> {
            goToAPI()
        }
    }

    @Step
    fun closeBrowser() {
        ontrackDSLWorld.clear()
        ontrackUtilityWorld.clear()
        pages.driver.quit()
    }

}