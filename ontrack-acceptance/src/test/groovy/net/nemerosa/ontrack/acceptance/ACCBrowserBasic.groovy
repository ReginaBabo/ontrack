package net.nemerosa.ontrack.acceptance


import net.nemerosa.ontrack.acceptance.browser.pages.BuildPage
import net.nemerosa.ontrack.acceptance.support.AcceptanceTest
import net.nemerosa.ontrack.acceptance.support.AcceptanceTestSuite
import org.junit.Test

import static net.nemerosa.ontrack.acceptance.steps.BasicSteps.loginAsAdmin
import static net.nemerosa.ontrack.test.TestUtils.uid
import static org.junit.Assert.assertEquals

/**
 * Basic GUI tests
 */
@AcceptanceTestSuite
@AcceptanceTest([AcceptanceTestContext.PRODUCTION, AcceptanceTestContext.SMOKE, AcceptanceTestContext.BROWSER_TEST])
class ACCBrowserBasic extends AcceptanceTestClient {

    @Test
    void 'Build page'() {
        def projectName = uid("P")
        ontrack.project(projectName) {
            branch("master") {
                def build = build("1")

                browser { browser ->
                    // Logs in
                    loginAsAdmin(browser)
                    // Goes to the build page which must contains the link
                    BuildPage buildPage = goTo(BuildPage, [id: build.id])
                    // Checks the title
                    assertEquals(
                            "Build 1",
                            buildPage.viewTitle
                    )
                }
            }
        }
    }

}
