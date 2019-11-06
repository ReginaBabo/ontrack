package net.nemerosa.ontrack.bdd.extension.stale

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.steps.AbstractOntrackDSL
import net.nemerosa.ontrack.bdd.model.support.getInt
import net.nemerosa.ontrack.bdd.model.support.getListOfStrings
import net.nemerosa.ontrack.bdd.model.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.extension.stale.dsl.StaleProperty
import net.nemerosa.ontrack.extension.stale.dsl.staleProperty
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackExtensionStaleSteps : AbstractOntrackDSL() {

    @Autowired
    private lateinit var ontrackDSLWorld: OntrackDSLWorld

    @Step
    fun checkStalePropertyNotDefined(projectRef: String) {
        val project = ontrackDSLWorld.projects[projectRef]
        assertNull(project.staleProperty, "Stale property not defined")
    }

    @Step
    fun setStaleProperty(projectRef: String, params: Map<String, String>) {
        val project = ontrackDSLWorld.projects[projectRef]
        project.staleProperty(params.toStaleProperty())
    }

    @Step
    fun checkStaleProperty(projectRef: String, params: Map<String, String>) {
        val project = ontrackDSLWorld.projects[projectRef]
        val expectedValue = params.toStaleProperty()
        val actualValue = project.staleProperty
        assertEquals(expectedValue, actualValue)
    }

    private fun Map<String, String>.toStaleProperty() =
            StaleProperty(
                    disablingDuration = getInt("disablingDuration") ?: 0,
                    deletingDuration = getInt("deletingDuration") ?: 0,
                    promotionsToKeep = getListOfStrings("promotionsToKeep") ?: emptyList()
            )
}