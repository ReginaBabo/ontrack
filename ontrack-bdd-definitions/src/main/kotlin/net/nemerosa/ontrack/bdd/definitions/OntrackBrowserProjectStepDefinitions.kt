package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserProjectSteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserProjectStepDefinitions {

    @Steps
    lateinit var ontrackBrowserProjectSteps: OntrackBrowserProjectSteps

    @When("""I create a branch "(.*)"""")
    fun createBranch(branchRef: String) {
        ontrackBrowserProjectSteps.createBranch(branchRef)
    }

    @Then("""branch "(.*)" is present in project page""")
    fun checkBranchInProjectPage(branchRef: String) {
        ontrackBrowserProjectSteps.checkBranchInProjectPage(branchRef)
    }

}