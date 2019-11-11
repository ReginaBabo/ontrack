package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.HomePage
import net.nemerosa.ontrack.bdd.model.pages.ProjectPage
import net.nemerosa.ontrack.bdd.model.worlds.OntrackDSLWorld
import net.nemerosa.ontrack.bdd.model.worlds.OntrackUtilityWorld
import net.thucydides.core.annotations.Step
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackBrowserProjectSteps : AbstractOntrackBrowserSteps() {

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
    fun goToProjectPage(projectRef: String) {
        currentPageAt<HomePage> {
            clickOnProject(ontrackUtilityWorld.replaceTokens(projectRef))
        }
    }

    @Step
    fun createBranch(branchRef: String) {
        currentPageAt<ProjectPage> {
            createBranch {
                name = ontrackUtilityWorld.replaceTokens(branchRef)
            }
        }
    }

    @Step
    fun checkBranchInProjectPage(branchRef: String) {
        currentPageAt<ProjectPage> {
            checkBranchIsPresent(ontrackUtilityWorld.replaceTokens(branchRef))
        }
    }

}