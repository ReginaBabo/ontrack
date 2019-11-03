package net.nemerosa.ontrack.bdd.pages

import net.thucydides.core.annotations.At
import org.openqa.selenium.WebDriver

@At(urls = ["#HOST", "#HOST/", "#HOST/#/home"])
class HomePage(driver: WebDriver) : CompletePage(driver) {
}