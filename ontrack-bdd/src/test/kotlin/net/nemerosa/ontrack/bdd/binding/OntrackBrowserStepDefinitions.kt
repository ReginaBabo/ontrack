package net.nemerosa.ontrack.bdd.binding

import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.binding.steps.OntrackBrowserSteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserStepDefinitions {

    @Steps
    lateinit var ontrackBrowserSteps: OntrackBrowserSteps

    @When("""login with the "(.*)" account""")
    fun login_with_account(accountRegisterName: String) {
        ontrackBrowserSteps.goToHomePage()
        ontrackBrowserSteps.login(accountRegisterName)
    }

}