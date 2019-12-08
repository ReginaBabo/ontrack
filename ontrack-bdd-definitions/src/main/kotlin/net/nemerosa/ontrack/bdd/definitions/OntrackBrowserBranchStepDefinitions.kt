package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Then
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserBranchSteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserBranchStepDefinitions {

    @Steps
    lateinit var ontrackBrowserBranchSteps: OntrackBrowserBranchSteps

    @Then("""branch page validation runs are loaded""")
    fun checkBranchPageValidationRunsLoaded() {
        ontrackBrowserBranchSteps.checkBranchPageValidationRunsLoaded()
    }

}