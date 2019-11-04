package net.nemerosa.ontrack.bdd.binding.extensions.stale

import cucumber.api.java.en.Then
import net.thucydides.core.annotations.Steps

class OntrackExtensionStaleStepDefinitions {

    @Steps
    lateinit var ontrackExtensionStaleSteps: OntrackExtensionStaleSteps

    @Then("""stale property on project "(.*)" is not defined""")
    fun stale_property_not_defined(projectRef: String) {
        ontrackExtensionStaleSteps.checkStalePropertyNotDefined(projectRef)
    }

}