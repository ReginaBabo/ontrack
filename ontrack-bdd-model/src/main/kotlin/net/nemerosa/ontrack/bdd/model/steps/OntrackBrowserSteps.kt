package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.CompletePage
import net.nemerosa.ontrack.bdd.model.pages.ProjectPage
import net.nemerosa.ontrack.bdd.model.pages.ValidationStampPage
import net.nemerosa.ontrack.bdd.model.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.nemerosa.ontrack.kdsl.model.branch
import net.nemerosa.ontrack.kdsl.model.findProjectByName
import net.nemerosa.ontrack.kdsl.model.validationStamp
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
        val vs = project {
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
    fun closeBrowser() {
        ontrackDSLWorld.clear()
        ontrackUtilityWorld.clear()
        pages.driver.quit()
    }

}