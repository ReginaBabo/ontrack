package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.model.findProjectByName
import net.nemerosa.ontrack.kdsl.model.getProjects
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import net.nemerosa.ontrack.test.support.uid
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringTest
class KdslTestProjects : AbstractKdslTest() {

    @Test
    fun `Project default description is empty`() {
        project {
            // Checks the project description
            assertEquals(
                    "",
                    ontrack.findProjectByName(this.name)?.description
            )
        }
    }

    @Test
    fun `List of projects`() {
        project {
            val p = ontrack.getProjects()
            assertTrue(this.name in p.map { it.name })
        }
    }

    @Test
    fun `Finding a project by name`() {
        project {
            assertNotNull(ontrack.findProjectByName(this.name))
            assertNull(ontrack.findProjectByName(uid("P")))
        }
    }

}