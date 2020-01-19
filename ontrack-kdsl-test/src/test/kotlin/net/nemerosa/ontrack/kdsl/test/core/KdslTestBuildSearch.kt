package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import net.nemerosa.ontrack.kdsl.test.support.withTestBranch
import org.junit.Test
import kotlin.test.assertEquals

@SpringTest
class KdslTestBuildSearch : AbstractKdslTest() {

    @Test
    fun `Build search default`() {
        withTestBranch { branch ->
            val builds = branch.project.searchBuilds()
            assertEquals(
                    listOf("3", "2", "1"),
                    builds.map { it.name }
            )
        }
    }

    @Test
    fun `Build search count`() {
        withTestBranch { branch ->
            val builds = branch.project.searchBuilds(maximumCount = 1)
            assertEquals(
                    listOf("3"),
                    builds.map { it.name }
            )
        }
    }

    @Test
    fun `Build search promotion`() {
        withTestBranch { branch ->
            val builds = branch.project.searchBuilds(promotionName = "BRONZE")
            assertEquals(
                    listOf("2"),
                    builds.map { it.name }
            )
        }
    }
}