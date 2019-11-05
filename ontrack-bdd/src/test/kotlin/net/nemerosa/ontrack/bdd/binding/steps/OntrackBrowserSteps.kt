package net.nemerosa.ontrack.bdd.binding.steps

import net.nemerosa.ontrack.bdd.BDDConfig
import net.nemerosa.ontrack.bdd.binding.steps.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.pages.CompletePage
import net.nemerosa.ontrack.bdd.pages.HomePage
import net.nemerosa.ontrack.bdd.pages.ValidationStampPage
import net.nemerosa.ontrack.kdsl.model.branch
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
    fun goToHomePage() {
        page<HomePage> {
            open()
        }
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
    fun closeBrowser() {
        ontrackDSLWorld.clear()
        pages.driver.quit()
    }

}