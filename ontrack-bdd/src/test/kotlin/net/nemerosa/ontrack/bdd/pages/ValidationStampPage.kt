package net.nemerosa.ontrack.bdd.pages

import net.thucydides.core.annotations.DefaultUrl
import org.openqa.selenium.WebDriver

@DefaultUrl("http://localhost:8080/#/validationStamp/{1}")
class ValidationStampPage(driver: WebDriver) : CompletePage(driver) {

    fun checkBuldUpdateCommandPresent() {
        checkElementPresent("#bulkUpdateValidationStamp")
    }

    /**
     * Opens this page for the given validation stamp ID
     */
    fun open(id: Int) {
        open(arrayOf(id.toString()))
    }

}