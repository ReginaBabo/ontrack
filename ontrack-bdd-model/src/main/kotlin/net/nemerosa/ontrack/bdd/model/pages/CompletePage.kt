package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.annotations.findby.By
import net.thucydides.core.annotations.WhenPageOpens
import org.openqa.selenium.WebDriver
import kotlin.test.fail

open class CompletePage(driver: WebDriver) : AbstractPage(driver) {

    @WhenPageOpens
    fun waitUntilEverythingIsLoaded() {
        waitForRenderedElementsToDisappear(By.className("ot-loading-indicator"))
    }

    fun userMenuLogin(username: String, password: String) {
        "Sign in".asLink?.click() ?: fail("Cannot find Sign in link.")

        "name".byName enter username
        "password".byName enter password

        "btn-primary".byClass.click()

        waitUntilEverythingIsLoaded()
        waitForPresenceOf("#header-user-menu")
    }

}
