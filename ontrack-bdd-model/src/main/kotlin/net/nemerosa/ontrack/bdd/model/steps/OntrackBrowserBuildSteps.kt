package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.BuildPage
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackBrowserBuildSteps : AbstractOntrackBrowserSteps() {

    @Autowired
    private lateinit var ontrackUtilityWorld: OntrackUtilityWorld

    @Step
    fun checkBuildPageTitle(value: String) {
        currentPageAt<BuildPage> {
            assertEquals(ontrackUtilityWorld.replaceTokens(value), viewTitle)
        }
    }

}