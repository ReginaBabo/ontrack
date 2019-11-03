package net.nemerosa.ontrack.bdd.binding

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.binding.steps.OntrackBrowserSteps
import net.nemerosa.ontrack.bdd.pages.ValidationStampPage
import net.thucydides.core.annotations.Steps

class OntrackBrowserStepDefinitions {

    @Steps
    lateinit var ontrackBrowserSteps: OntrackBrowserSteps

    @When("""login with the "(.*)" account""")
    fun login_with_account(accountRegisterName: String) {
        ontrackBrowserSteps.goToHomePage()
        ontrackBrowserSteps.loginWithAccount(accountRegisterName)
    }

    @When("""going to the page of the validation stamp "(.*)" in branch "(.*)" of project "(.*)"""")
    fun go_to_validation_stamp_page(
            validationStampName: String,
            branchName: String,
            projectRegisterName: String
    ) {
        ontrackBrowserSteps.goToValidationStampPage(validationStampName, branchName, projectRegisterName)
    }

    @Then("""check that the validation stamp page contains the bulk update command""")
    fun check_validation_stamp_page_contains_bulk_update_command() {
        ontrackBrowserSteps.checkBuldUpdateCommandPresentInValidationStampPage()
    }

}