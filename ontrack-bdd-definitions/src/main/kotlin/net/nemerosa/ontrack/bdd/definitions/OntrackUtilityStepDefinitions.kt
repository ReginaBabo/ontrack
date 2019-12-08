package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Given
import net.nemerosa.ontrack.bdd.model.steps.OntrackUtilitySteps
import net.thucydides.core.annotations.Steps

class OntrackUtilityStepDefinitions {

    @Steps
    lateinit var ontrackUtilitySteps: OntrackUtilitySteps

    @Given("""a unique name for (.*) "(.*)"""")
    fun uniqueName(group: String, key: String) {
        ontrackUtilitySteps.uniqueName(group, key)
    }

    @Given("""a (.*) characters long name for (.*) "(.*)"""")
    fun uniqueLongName(length: Int, group: String, key: String) {
        ontrackUtilitySteps.uniqueLongName(length, group, key)
    }

}