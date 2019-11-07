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

}