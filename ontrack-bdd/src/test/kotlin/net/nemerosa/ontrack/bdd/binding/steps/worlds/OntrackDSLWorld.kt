package net.nemerosa.ontrack.bdd.binding.steps.worlds

import net.nemerosa.ontrack.kdsl.model.Project
import org.springframework.stereotype.Component

@Component
class OntrackDSLWorld {

    /**
     * Register of projects
     */
    val projects = mutableMapOf<String, Project>()

    /**
     * Gets a registered project
     */
    fun getProject(projectRegisterName: String): Project =
            projects[projectRegisterName]
                    ?: throw IllegalStateException("Cannot find project registered with name $projectRegisterName")

}