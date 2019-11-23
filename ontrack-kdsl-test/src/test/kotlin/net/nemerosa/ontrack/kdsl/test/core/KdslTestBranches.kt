package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.model.branch
import net.nemerosa.ontrack.kdsl.model.branches
import net.nemerosa.ontrack.kdsl.model.findProjectByName
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import net.nemerosa.ontrack.test.support.uid
import org.junit.Test
import kotlin.test.assertEquals

@SpringTest
class KdslTestBranches : AbstractKdslTest() {

    @Test
    fun `Project branches`() {
        val name = uid("P")
        // Project and two branches
        project {
            (1..5).forEach {
                branch("B$it")
            }
        }
        // Checks the branches
        assertEquals(
                (5 downTo 1).map { "B$it" },
                ontrack.findProjectByName(name)?.branches()?.map { it.name }
        )
    }

}