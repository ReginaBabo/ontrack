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

    @Given("""a branch "(.*)" in project "(.*)"""")
    fun branch_available(branchName: String, projectRegisterName: String) {
        ontrackDSLSteps.createBranchInProject(branchName, projectRegisterName)
    }

    @Given("""a validation stamp "(.*)" in branch "(.*)" of project "(.*)"""")
    fun validation_stamp_available(validationStampName: String, branchName: String, projectRegisterName: String) {
        ontrackDSLSteps.createValidationStampInBranchAndProject(validationStampName, branchName, projectRegisterName)
    }

    @Given("""a "(.*)" account belonging to the "(.*)" account group""")
    fun account_in_group_available(accountRegisterName: String, accountGroupRegisterName: String) {
        TODO()
    }

    @Given("""a "(.*)" account group""")
    fun account_group_available(accountGroupRegisterName: String) {
        ontrackDSLSteps.createAndRegisterAccountGroup(accountGroupRegisterName)
    }

}