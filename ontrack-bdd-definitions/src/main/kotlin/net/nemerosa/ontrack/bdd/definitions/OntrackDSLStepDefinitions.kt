package net.nemerosa.ontrack.bdd.definitions

import cucumber.api.java.en.Given
import net.nemerosa.ontrack.bdd.model.steps.OntrackDSLSteps
import net.thucydides.core.annotations.Steps

class OntrackDSLStepDefinitions {

    @Steps
    lateinit var ontrackDSLSteps: OntrackDSLSteps

    @Given("""^a project "(.*)"$""")
    fun project_available(name: String) {
        ontrackDSLSteps.createAndRegisterProject(name)
    }

    @Given("""^a big branch "(.*)"$""")
    fun big_branch_available(ref: String) {
        ontrackDSLSteps.createAndRegisterBigBranch(ref)
    }

    @Given("""a branch "(.*)" in project "(.*)"""")
    fun branch_available(branchName: String, projectRegisterName: String) {
        ontrackDSLSteps.createBranchInProject(branchName, projectRegisterName)
    }

    @Given("""a build "(.*)"""")
    fun build_available(buildRef: String) {
        ontrackDSLSteps.createAndRegisterBuild(buildRef)
    }

    @Given("""a validation stamp "(.*)" in branch "(.*)" of project "(.*)"""")
    fun validation_stamp_available(validationStampName: String, branchName: String, projectRegisterName: String) {
        ontrackDSLSteps.createValidationStampInBranchAndProject(validationStampName, branchName, projectRegisterName)
    }

    @Given("""an account "(.*)" belonging to the "(.*)" account group""")
    fun account_in_group_available(accountRegisterName: String, accountGroupRegisterName: String) {
        ontrackDSLSteps.createAndRegisterAccountInGroup(accountRegisterName, accountGroupRegisterName)
    }

    @Given("""an account "(.*)" having "(.*)" as password""")
    fun account_with_password(accountRegisterName: String, password: String) {
        ontrackDSLSteps.createAndRegisterAccountWithPassword(accountRegisterName, password)
    }

    @Given("""a "(.*)" account group""")
    fun account_group_available(accountGroupRegisterName: String) {
        ontrackDSLSteps.createAndRegisterAccountGroup(accountGroupRegisterName)
    }

    @Given("""the "(.*)" account group is granted the "(.*)" role""")
    fun account_group_global_permission(accountGroupRegisterName: String, role: String) {
        ontrackDSLSteps.setAccountGroupGlobalPermission(accountGroupRegisterName, role)
    }

}