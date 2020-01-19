package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.model.build
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import net.nemerosa.ontrack.kdsl.test.support.withTestBranch
import net.nemerosa.ontrack.test.support.assertAll
import org.junit.Test
import kotlin.test.assertEquals

@SpringTest
class KdslTestPromotionRuns : AbstractKdslTest() {

    @Test
    fun `Promotion runs`() {
        withTestBranch { branch ->
            val run = branch.build("2").promote("BRONZE")
            assertEquals("BRONZE", run.promotionLevel.name)
            // List of runs
            val runs = ontrack.build(branch.project.name, branch.name, "2").promotionRuns
            assertEquals(2, runs.size)
            assertAll(runs) {
                it.promotionLevel.name == "BRONZE"
            }
        }
    }

    @Test
    fun `Promotion run deletion`() {
        withTestBranch { branch ->
            // Creates two runs
            val build = ontrack.build(branch.project.name, branch.name, "2")
            build.promote("BRONZE")
            build.promote("BRONZE")
            // Gets the promotion runs
            val runs = build.promotionRuns
            assertEquals(3, runs.size)
            val run = runs[0]
            run.delete()
            val newRuns = build.promotionRuns
            assertEquals(2, newRuns.size)
        }
    }

}