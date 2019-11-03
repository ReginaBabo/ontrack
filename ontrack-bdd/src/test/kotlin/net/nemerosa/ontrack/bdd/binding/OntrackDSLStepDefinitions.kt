package net.nemerosa.ontrack.bdd.binding

import cucumber.api.java.en.Given
import net.nemerosa.ontrack.bdd.binding.steps.OntrackDSLSteps
import net.thucydides.core.annotations.Steps

class OntrackDSLStepDefinitions {

    @Steps
    lateinit var ontrackDSLSteps: OntrackDSLSteps

    @Given("""a project "(.*)"""")
    fun project_available(name: String) {
        ontrackDSLSteps.createAndRegisterProject(name)
    }

}