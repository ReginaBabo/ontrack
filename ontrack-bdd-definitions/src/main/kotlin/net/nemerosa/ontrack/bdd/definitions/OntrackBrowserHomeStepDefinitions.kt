package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.After
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserSteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserHomeStepDefinitions {

    @Steps
    lateinit var ontrackBrowserSteps: OntrackBrowserSteps

    @When("""I create a project "(.*)"""")
    fun createProject(projectRef: String) {
        ontrackBrowserSteps.createProject(projectRef)
    }

    @Then("""project "(.*)" is present in home page""")
    fun checkProjectInHomePage(projectRef: String) {
        ontrackBrowserSteps.checkProjectInHomePage(projectRef)
    }

}