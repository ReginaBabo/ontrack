package net.nemerosa.ontrack.bdd.model.pages

import net.thucydides.core.annotations.DefaultUrl
import org.openqa.selenium.WebDriver

@DefaultUrl("http://localhost:8080/#/admin-accounts")
class AccountManagementPage(driver: WebDriver) : CompletePage(driver) {
}