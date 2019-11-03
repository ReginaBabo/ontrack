package net.nemerosa.ontrack.bdd.binding.steps

import net.nemerosa.ontrack.bdd.binding.steps.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.support.uid
import net.nemerosa.ontrack.kdsl.model.project
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
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
}