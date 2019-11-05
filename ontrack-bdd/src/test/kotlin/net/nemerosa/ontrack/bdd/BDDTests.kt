package net.nemerosa.ontrack.bdd

import cucumber.api.CucumberOptions
import net.nemerosa.ontrack.bdd.model.BDDConfig
import net.serenitybdd.cucumber.CucumberWithSerenity
import net.serenitybdd.junit.spring.integration.SpringIntegrationClassRule
import net.thucydides.core.annotations.Managed
import net.thucydides.core.annotations.ManagedPages
import net.thucydides.core.pages.Pages
import org.junit.Rule
import org.junit.runner.RunWith
import org.openqa.selenium.WebDriver
import org.springframework.test.context.ContextConfiguration

@RunWith(CucumberWithSerenity::class)
@CucumberOptions(features = ["classpath:features"])
@ContextConfiguration(classes = [BDDConfig::class])
class BDDTests {

    @Rule
    val springIntegration = SpringIntegrationClassRule()

    @Managed
    lateinit var webDriver: WebDriver

    @ManagedPages
    lateinit var pages: Pages

}