package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserAccountManagementSteps
import net.thucydides.core.annotations.Steps


class OntrackBrowserAccountManagementStepDefinitions {

    @Steps
    lateinit var browserAccountManagementSteps: OntrackBrowserAccountManagementSteps

    @When("""I go to account management page""")
    fun goToAccountManagementPage() {
        browserAccountManagementSteps.goToAccountManagementPage()
    }

    @Then("""I am on the account management page""")
    fun checkOnAccountManagementPage() {
        browserAccountManagementSteps.checkOnAccountManagementPage()
    }
}