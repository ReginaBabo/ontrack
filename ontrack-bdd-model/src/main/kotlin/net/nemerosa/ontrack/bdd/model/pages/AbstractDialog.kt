package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.pages.WebElementFacade
import org.openqa.selenium.By
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractDialog<D : AbstractDialog<D>>(parent: AbstractPage) : AbstractModule(parent) {

    private val okButton by element(By.className("ot-dialog-ok"))
    private val errorMessage by element(By.className("ot-alert-error"))

    fun waitFor(): D {
        parent.element<WebElementFacade>(".ot-dialog-ok").waitUntilVisible<WebElementFacade>()
        @Suppress("UNCHECKED_CAST")
        return this as D
    }

    fun ok() {
        assertTrue(okButton.isEnabled)
        okButton.click()
    }

    fun checkError(message: String) {
        assertTrue(errorMessage.isVisible)
        assertEquals(message.trim(), errorMessage.textContent.trim())
    }

}