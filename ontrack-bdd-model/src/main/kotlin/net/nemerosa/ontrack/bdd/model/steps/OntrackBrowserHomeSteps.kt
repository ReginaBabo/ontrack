package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.HomePage
import net.nemerosa.ontrack.bdd.model.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackBrowserHomeSteps : AbstractOntrackBrowserSteps() {

    @Autowired
    private lateinit var ontrackDSLWorld: OntrackDSLWorld

    @Autowired
    private lateinit var ontrackUtilityWorld: OntrackUtilityWorld

    @Step
    fun goToHomePage() {
        page<HomePage> {
            open()
        }
    }

    @Step
    fun createProject(projectRef: String) {
        currentPageAt<HomePage> {
            createProject {
                name = ontrackUtilityWorld.replaceTokens(projectRef)
            }
            waitForPage()
        }
    }

    @Step
    fun checkProjectInHomePage(projectRef: String) {
        currentPageAt<HomePage> {
            checkProjectIsPresent(ontrackUtilityWorld.replaceTokens(projectRef))
        }
    }

    @Step
    fun checkProjectDialogInError(message: String) {
        currentPageAt<HomePage> {
            checkProjectDialogInError(ontrackUtilityWorld.replaceTokens(message))
        }
    }

    @Step
    fun navigateToProjectPage(projectRef: String) {
        currentPageAt<HomePage> {
            clickOnProject(ontrackUtilityWorld.replaceTokens(projectRef))
        }
    }

    @Step
    fun checkOnHomePage() {
        currentPageAt<HomePage> { }
    }

}