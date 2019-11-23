package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.annotations.findby.By
import net.thucydides.core.annotations.WhenPageOpens
import org.openqa.selenium.WebDriver

open class CompletePage(driver: WebDriver) : AbstractPage(driver) {

    @WhenPageOpens
    fun waitUntilEverythingIsLoaded() {
        waitForRenderedElementsToDisappear(By.className("ot-loading-indicator"))
    }

    fun userMenuLogin(username: String, password: String) {
        "Sign in".asLink.click()

        "name".byName enter username
        "password".byName enter password

        "btn-primary".byClass.click()

        waitUntilEverythingIsLoaded()
        "#header-user-menu".waitUntilVisible()
    }

}
