package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.model.intervalFilter
import net.nemerosa.ontrack.kdsl.model.lastPromotedBuilds
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import net.nemerosa.ontrack.kdsl.test.support.withTestBranch
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun `Filter interval`() {
        withTestBranch {
            val results = it.intervalFilter(from = "3", to = "1")
            assertEquals(
                    listOf("3", "2", "1"),
                    results.map { it.name }
            )
        }
    }

    @Test
    fun `Filter interval in reverse order`() {
        withTestBranch {
            val results = it.intervalFilter(from = "1", to = "3")
            assertEquals(
                    listOf("3", "2", "1"),
                    results.map { it.name }
            )
        }
    }

    @Test
    fun `Filter interval - only two`() {
        withTestBranch {
            val results = it.intervalFilter(from = "2", to = "3")
            assertEquals(
                    listOf("3", "2"),
                    results.map { it.name }
            )
        }
    }

    @Test
    fun `Filter interval - only one`() {
        withTestBranch {
            val results = it.intervalFilter(from = "2", to = "2")
            assertEquals(
                    listOf("2"),
                    results.map { it.name }
            )
        }
    }

    @Test
    fun `Filter interval - not existing`() {
        withTestBranch {
            val results = it.intervalFilter(from = "2", to = "4")
            assertTrue(
                    results.isEmpty(),
                    "No build is returned"
            )
        }
    }

}