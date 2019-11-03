package net.nemerosa.ontrack.bdd.pages

import net.thucydides.core.annotations.At
import org.openqa.selenium.WebDriver

@At(urls = ["#HOST/#/validationStamp/{0}"])
class ValidationStampPage(driver: WebDriver) : CompletePage(driver) {
}