package net.nemerosa.ontrack.bdd.binding.extensions.stale

import net.nemerosa.ontrack.bdd.BDDConfig
import net.nemerosa.ontrack.bdd.binding.steps.worlds.OntrackDSLWorld
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertNull
import net.nemerosa.ontrack.extension.stale.dsl.StaleKDSLExtensionsKt.getStaleProperty

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackExtensionStaleSteps {

    @Autowired
    private lateinit var ontrackDSLWorld: OntrackDSLWorld

    fun checkStalePropertyNotDefined(projectRef: String) {
        val project = ontrackDSLWorld.projects[projectRef]
        assertNull(project.staleProperty, "Stale property not defined")
    }
}