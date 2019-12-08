package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Then
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserBuildSteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserBuildStepDefinitions {

    @Steps
    lateinit var ontrackBrowserBuildSteps: OntrackBrowserBuildSteps

    @Then("""build page title is "(.*)"""")
    fun checkBuildPageTitle(value: String) {
        ontrackBrowserBuildSteps.checkBuildPageTitle(value)
    }

}