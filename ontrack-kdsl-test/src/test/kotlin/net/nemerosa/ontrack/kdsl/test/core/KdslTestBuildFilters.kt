package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.model.*
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import org.junit.Test
import kotlin.test.assertEquals

@SpringTest
class KdslTestBuildFilters : AbstractKdslTest() {

    @Test
    fun `Getting last promoted build`() {
        withTestBranch { branch ->
            val results = branch.lastPromotedBuilds
            assertEquals(
                    listOf("3", "2", "1"),
                    results.map { it.name }
            )
        }
    }

    private fun withTestBranch(code: (Branch) -> Unit) {
        branch {
            // Promotions
            promotionLevel("COPPER", "Copper promotion")
            promotionLevel("BRONZE", "Bronze promotion")
            promotionLevel("GOLD", "Gold promotion")
            // Validation stamps
            validationStamp("SMOKE")
            validationStamp("REGRESSION")
            // Builds and their promotions
            build("1") {
                promote("GOLD")
            }
            build("2") {
                promote("BRONZE")
            }
            build("3") {
                promote("COPPER")
            }
        }
    }

}