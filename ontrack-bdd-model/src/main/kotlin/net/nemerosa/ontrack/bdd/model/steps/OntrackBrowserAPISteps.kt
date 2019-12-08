package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.APIPage
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackBrowserAPISteps : AbstractOntrackBrowserSteps() {

    @Autowired
    private lateinit var ontrackUtilityWorld: OntrackUtilityWorld

    @Step
    fun checkAPILink(link: String) {
        currentPageAt<APIPage> {
            val value = "ot-api-link".byClass.text
            val expectedValue = ontrackUtilityWorld.replaceTokens(link)
            assertEquals(expectedValue, value, "API link expected to be $expectedValue and is $value")
        }
    }

}