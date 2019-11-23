package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.annotations.findby.By
import net.serenitybdd.core.pages.WebElementFacade
import net.thucydides.core.annotations.DefaultUrl
import org.openqa.selenium.WebDriver

@DefaultUrl("http://localhost:8080/#/project/{1}")
class ProjectPage(driver: WebDriver) : CompletePage(driver) {

    /**
     * Opens this page for the given project ID
     */
    fun open(id: Int) {
        open(arrayOf(id.toString()))
    }

    /**
     * Creating a branch from the project page
     */
    fun createBranch(configure: BranchDialog.() -> Unit): BranchDialog {
        val createBranchCommand = "ot-command-branch-new".byClass
        createBranchCommand.click()
        val dialog = BranchDialog(this).waitFor()
        dialog.configure()
        dialog.ok()
        // Returns the dialog
        return dialog
    }

    fun checkBranchIsPresent(name: String) {
        name.asLink.waitUntilVisible<WebElementFacade>()
    }

}

class BranchDialog(parent: AbstractPage) : AbstractDialog<BranchDialog>(parent) {

    var name: String by textInput(By.name("name"))

}