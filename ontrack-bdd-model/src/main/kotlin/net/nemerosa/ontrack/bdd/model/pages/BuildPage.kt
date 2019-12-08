package net.nemerosa.ontrack.bdd.model.pages

import net.thucydides.core.annotations.DefaultUrl
import org.openqa.selenium.WebDriver

@DefaultUrl("http://localhost:8080/#/build/{1}")
class BuildPage(driver: WebDriver) : CompletePage(driver) {

    /**
     * Opens this page for the given build ID
     */
    fun open(id: Int) {
        open(arrayOf(id.toString()))
    }

    /**
     * View title
     */
    val viewTitle: String
        get() = "build-page-title".byId.text

}
