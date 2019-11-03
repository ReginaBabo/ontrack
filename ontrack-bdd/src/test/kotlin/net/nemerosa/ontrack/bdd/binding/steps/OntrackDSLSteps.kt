package net.nemerosa.ontrack.bdd.binding.steps

import net.nemerosa.ontrack.bdd.BDDConfig
import net.nemerosa.ontrack.bdd.binding.steps.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.support.uid
import net.nemerosa.ontrack.kdsl.model.branch
import net.nemerosa.ontrack.kdsl.model.project
import net.nemerosa.ontrack.kdsl.model.validationStamp
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackDSLSteps : AbstractOntrackDSL() {

    @Autowired
    private lateinit var ontrackDSLWorld: OntrackDSLWorld

    @Step
    fun createAndRegisterProject(name: String) {
        // Actual name of the project
        val projectName = uid("P")
        // Gets or creates the project, registers it and returns it
        ontrack.project(projectName).apply {
            ontrackDSLWorld.projects[name] = this
        }
    }

    @Step
    fun createBranchInProject(branchName: String, projectRegisterName: String) {
        // Gets the project
        val project = ontrackDSLWorld.getProject(projectRegisterName)
        // Creates a branch in this project
        project.branch(branchName)
    }

    @Step
    fun createValidationStampInBranchAndProject(validationStampName: String, branchName: String, projectRegisterName: String) {
        // Gets the project
        val project = ontrackDSLWorld.getProject(projectRegisterName)
        // Creates/gets the branch in this project and creates a validation stamp
        project.branch(branchName) {
            validationStamp(validationStampName)
        }
    }
}
