package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.After
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.model.pages.ProjectPage
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserHomeSteps
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserSteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserStepDefinitions {

    @Steps
    lateinit var ontrackBrowserHomeSteps: OntrackBrowserHomeSteps

    @Steps
    lateinit var ontrackBrowserSteps: OntrackBrowserSteps

    @When("""login with the "(.*)" account""")
    fun login_with_account(accountRegisterName: String) {
        ontrackBrowserHomeSteps.goToHomePage()
        ontrackBrowserSteps.loginWithAccount(accountRegisterName)
        ontrackBrowserHomeSteps.goToHomePage()
    }

    @When("""login as admin""")
    fun login_as_admin() {
        ontrackBrowserHomeSteps.goToHomePage()
        ontrackBrowserSteps.loginAsAdmin()
        ontrackBrowserHomeSteps.goToHomePage()
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

    @When("""I go to project page for "(.*)"""")
    fun goToProjectPage(projectRef: String) {
        ontrackBrowserSteps.goToProjectPage(projectRef)
    }

    @When("""I go to branch page for "(.*)"""")
    fun goToBranchPage(branchRef: String) {
        ontrackBrowserSteps.goToBranchPage(branchRef)
    }

    @When("""I go to build page for "(.*)"""")
    fun goToBuildPage(buildRef: String) {
        ontrackBrowserSteps.goToBuildPage(buildRef)
    }

    @When("""I navigate to API page""")
    fun navigateToAPIPage() {
        ontrackBrowserSteps.navigateToAPIPage()
    }

    @After
    fun closing_browser() {
        ontrackBrowserSteps.closeBrowser()
    }

}