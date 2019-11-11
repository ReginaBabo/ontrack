package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserHomeSteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserHomeStepDefinitions {

    @Steps
    lateinit var ontrackBrowserHomeSteps: OntrackBrowserHomeSteps

    @When("""I create a project "(.*)"""")
    fun createProject(projectRef: String) {
        ontrackBrowserHomeSteps.createProject(projectRef)
    }

    @Then("""project "(.*)" is present in home page""")
    fun checkProjectInHomePage(projectRef: String) {
        ontrackBrowserHomeSteps.checkProjectInHomePage(projectRef)
    }

    @Then("""project dialog shows error "(.*)"""")
    fun checkProjectDialogInError(message: String) {
        ontrackBrowserHomeSteps.checkProjectDialogInError(message)
    }

}