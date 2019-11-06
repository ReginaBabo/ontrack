package net.nemerosa.ontrack.bdd.extension.stale

import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.nemerosa.ontrack.bdd.extension.stale.OntrackExtensionStaleSteps
import net.thucydides.core.annotations.Steps

class OntrackExtensionStaleStepDefinitions {

    @Steps
    lateinit var ontrackExtensionStaleSteps: OntrackExtensionStaleSteps

    @When("""stale property on project "(.*)" is set to:""")
    fun set_stale_property(projectRef: String, params: Map<String, String>) {
        ontrackExtensionStaleSteps.setStaleProperty(projectRef, params)
    }

    @Then("""stale property on project "(.*)" is not defined""")
    fun stale_property_not_defined(projectRef: String) {
        ontrackExtensionStaleSteps.checkStalePropertyNotDefined(projectRef)
    }

    @Then("""stale property on project "(.*)" is equal to:""")
    fun check_stale_property(projectRef: String, params: Map<String, String>) {
        ontrackExtensionStaleSteps.checkStaleProperty(projectRef, params)
    }

}