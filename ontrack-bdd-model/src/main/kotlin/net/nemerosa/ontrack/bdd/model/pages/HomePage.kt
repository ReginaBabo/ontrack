package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.annotations.findby.By
import net.serenitybdd.core.pages.WebElementFacade
import net.thucydides.core.annotations.At
import net.thucydides.core.annotations.WhenPageOpens
import org.openqa.selenium.WebDriver

@At(urls = ["#HOST", "#HOST/", "#HOST/#/home"])
class HomePage(driver: WebDriver) : CompletePage(driver) {

    @WhenPageOpens
    fun waitForBreadcrumbs() {
        waitForPresenceOf(".ot-command-api")
    }

    fun createProject(configure: ProjectDialog.() -> Unit): ProjectDialog {
        val newProjectCommand = "ot-command-project-new".byClass
        newProjectCommand.waitUntilClickable<WebElementFacade>()
        newProjectCommand.click()
        val dialog = ProjectDialog(this).waitFor()
        dialog.configure()
        dialog.ok()
        // Returns the dialog
        return dialog
    }

    fun checkProjectIsPresent(name: String) {
        name.asLink.waitUntilVisible<WebElementFacade>()
    }

    fun checkProjectDialogInError(message: String) {
        ProjectDialog(this).checkError(message)
    }

    fun clickOnProject(name: String) {
        name.asLink.click()
    }

}

class ProjectDialog(parent: AbstractPage) : AbstractDialog<ProjectDialog>(parent) {

    var name: String by textInput(By.name("name"))

}