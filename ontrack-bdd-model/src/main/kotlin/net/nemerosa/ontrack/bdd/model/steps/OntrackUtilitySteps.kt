package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackUtilitySteps() {

    @Autowired
    private lateinit var ontrackUtilityWorld: OntrackUtilityWorld

    @Step
    fun uniqueName(group: String, key: String) {
        ontrackUtilityWorld.uniqueName(group, key)
    }

    @Step
    fun uniqueLongName(length: Int, group: String, key: String) {
        ontrackUtilityWorld.uniqueLongName(length, group, key)
    }

}