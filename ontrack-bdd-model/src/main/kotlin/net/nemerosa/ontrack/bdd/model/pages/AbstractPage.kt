package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.annotations.findby.By
import net.serenitybdd.core.pages.WebElementFacade
import net.thucydides.core.pages.PageObject
import org.openqa.selenium.WebDriver
import kotlin.test.assertTrue
import kotlin.test.fail

abstract class AbstractPage(driver: WebDriver) : PageObject(driver) {

    /**
     * Generic sync for the page
     */
    fun waitForPage() = callWhenPageOpensMethods()

    /**
     * Given some text, returns a link element whole text is matching. Returns null if no match.
     */
    val String.asLink: WebElementFacade
        get() = find<WebElementFacade>(By.linkText(this)).waitUntilVisible()

    /**
     * Given a form name, returns the required associated element
     */
    val String.byName: WebElementFacade
        get() = find<WebElementFacade>(By.name(this))?.waitUntilVisible()
                ?: fail("Cannot find element with name = $this")

    /**
     * Given a class name, returns the required associated element
     */
    val String.byClass: WebElementFacade
        get() = find<WebElementFacade>(By.className(this))?.waitUntilVisible()
                ?: fail("Cannot find element with class = $this")

    /**
     * Given an ID, returns the required associated element
     */
    val String.byId: WebElementFacade
        get() = find<WebElementFacade>(By.id(this))?.waitUntilVisible()
                ?: fail("Cannot find element with ID = $this")

    /**
     * Waits for the element to be visible
     */
    fun String.waitUntilVisible() {
        findBy<WebElementFacade>(this).waitUntilVisible<WebElementFacade>()
    }

    /**
     * DSL form to type some text into an element
     */
    infix fun WebElementFacade.enter(text: String) {
        typeInto(this, text)
    }

    /**
     * Checks if an element is present or not
     */
    fun checkElementPresent(css: String) {
        val e: WebElementFacade? = find<WebElementFacade>(By.cssSelector(css))?.waitUntilVisible()
        assertTrue(e != null && e.isDisplayed, "Element at [$css] is not present.")
    }
}