package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.annotations.findby.By
import net.thucydides.core.annotations.WhenPageOpens
import org.openqa.selenium.WebDriver
import kotlin.test.assertTrue

open class CompletePage(driver: WebDriver) : AbstractPage(driver) {

    @WhenPageOpens
    fun waitUntilEverythingIsLoaded() {
        waitForRenderedElementsToDisappear(By.className("ot-loading-indicator"))
    }

    fun checkLoginIsVisible() {
        assertTrue("name".byName.isVisible)
        assertTrue("password".byName.isVisible)
    }

    fun userMenuLogin(username: String, password: String) {
        "Sign in".asLink.click()
        login(username, password)
    }

    fun login(username: String, password: String) {
        "name".byName enter username
        "password".byName enter password

        "btn-primary".byClass.click()

        waitForPage()
        "#header-user-menu".waitUntilVisible()
    }

    fun goToAPI() {
        "ot-command-api".byClass.click()
    }

}
