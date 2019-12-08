package net.nemerosa.ontrack.bdd.model.pages

import net.thucydides.core.annotations.DefaultUrl
import org.openqa.selenium.WebDriver

@DefaultUrl("http://localhost:8080/#/branch/{1}")
class BranchPage(driver: WebDriver) : CompletePage(driver) {

    /**
     * Opens this page for the given branch ID
     */
    fun open(id: Int) {
        open(arrayOf(id.toString()))
    }

    fun checkValidationRunsLoaded() {
        "img.ot-validation-run-status".byCss
    }

}
