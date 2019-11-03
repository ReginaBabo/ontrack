package net.nemerosa.ontrack.bdd.binding.steps

import net.nemerosa.ontrack.bdd.pages.HomePage
import net.thucydides.core.annotations.Step
import org.springframework.stereotype.Component

@Component
class OntrackBrowserSteps : AbstractOntrackBrowserSteps() {

    @Step
    fun login(accountRegisterName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Step
    fun goToHomePage() {
        page<HomePage> {
            open()
        }
    }
}