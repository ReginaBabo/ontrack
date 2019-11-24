package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.model.lastPromotedBuilds
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import net.nemerosa.ontrack.kdsl.test.support.withTestBranch
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

}