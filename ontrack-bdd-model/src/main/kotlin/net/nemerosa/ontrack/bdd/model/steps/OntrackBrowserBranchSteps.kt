package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.BranchPage
import net.thucydides.core.annotations.Step
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackBrowserBranchSteps : AbstractOntrackBrowserSteps() {

    @Step
    fun checkBranchPageValidationRunsLoaded() {
        currentPageAt<BranchPage> {
            checkValidationRunsLoaded()
        }
    }

}