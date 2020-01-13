package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.nemerosa.ontrack.bdd.model.pages.AccountManagementPage
import net.thucydides.core.annotations.Step
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration

@Component
@ContextConfiguration(classes = [BDDConfig::class])
class OntrackBrowserAccountManagementSteps : AbstractOntrackBrowserSteps() {

    /**
     * Directly goes to the "Account management" page
     */
    @Step
    fun goToAccountManagementPage() {
        openPage<AccountManagementPage>()
    }

    fun checkOnAccountManagementPage() {
        currentPageAt<AccountManagementPage> { }
    }

}