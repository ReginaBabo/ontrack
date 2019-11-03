package net.nemerosa.ontrack.bdd.pages

import net.serenitybdd.core.annotations.findby.By
import net.serenitybdd.core.pages.WebElementFacade
import net.thucydides.core.pages.PageObject
import org.openqa.selenium.WebDriver
import kotlin.test.fail

abstract class AbstractPage(driver: WebDriver) : PageObject(driver) {

    /**
     * Given some text, returns a link element whole text is matching. Returns null if no match.
     */
    val String.asLink: WebElementFacade?
        get() = find<WebElementFacade>(By.linkText(this))?.waitUntilVisible()

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
     * DSL form to type some text into an element
     */
    infix fun WebElementFacade.enter(text: String) {
        typeInto(this, text)
    }
}