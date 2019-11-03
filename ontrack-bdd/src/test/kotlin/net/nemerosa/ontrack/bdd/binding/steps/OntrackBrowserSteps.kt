package net.nemerosa.ontrack.bdd.binding.steps

import net.nemerosa.ontrack.bdd.BDDConfig
import net.nemerosa.ontrack.bdd.binding.steps.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.pages.CompletePage
import net.nemerosa.ontrack.bdd.pages.HomePage
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
    fun goToHomePage() {
        page<HomePage> {
            open()
        }
    }
}