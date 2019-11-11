package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.annotations.findby.By
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
        val createProjectCommand = "ot-command-project-new".byClass
        createProjectCommand.click()
        val dialog = ProjectDialog(this).waitFor()
        dialog.configure()
        dialog.ok()
        // Returns the dialog
        return dialog
    }

}

class ProjectDialog(parent: AbstractPage) : AbstractDialog<ProjectDialog>(parent) {

    var name: String by textInput(By.name("name"))

}