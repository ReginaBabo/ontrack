package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Then
import net.nemerosa.ontrack.bdd.model.steps.OntrackBrowserAPISteps
import net.thucydides.core.annotations.Steps

class OntrackBrowserAPIStepDefinitions {

    @Steps
    lateinit var ontrackBrowserAPISteps: OntrackBrowserAPISteps

    @Then("""link on API page is "(.*)"""")
    fun checkAPILink(link: String) {
        ontrackBrowserAPISteps.checkAPILink(link)
    }

}