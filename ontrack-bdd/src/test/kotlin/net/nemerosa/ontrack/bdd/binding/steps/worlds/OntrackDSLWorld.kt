package net.nemerosa.ontrack.bdd.binding.steps.worlds

import net.nemerosa.ontrack.kdsl.model.Project
import org.springframework.stereotype.Component

@Component
class OntrackDSLWorld {

    /**
     * Register of projects
     */
    val projects = mutableMapOf<String, Project>()

}