package net.nemerosa.ontrack.bdd

import cucumber.api.CucumberOptions
import net.serenitybdd.cucumber.CucumberWithSerenity
import net.serenitybdd.junit.spring.integration.SpringIntegrationClassRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(CucumberWithSerenity::class)
@CucumberOptions(features = ["classpath:features"])
@ContextConfiguration(classes = [BDDConfig::class])
class BDDTests {

    @Rule
    val springIntegration = SpringIntegrationClassRule()

}