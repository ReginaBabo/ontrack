package net.nemerosa.ontrack.kdsl.test.core

import net.nemerosa.ontrack.kdsl.model.build
import net.nemerosa.ontrack.kdsl.model.nextBuild
import net.nemerosa.ontrack.kdsl.model.previousBuild
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringTest
class KdslTestBuilds : AbstractKdslTest() {

    @Test
    fun `Build previous and next`() {
        // Project and branch
        branch {
            val build = (1..3).map {
                build("$it")
            }
            // Build 1 has no previous build
            assertNull(build[0].previousBuild)
            assertEquals("2", build[0].nextBuild?.name)
            // Build 2 has previous and next builds
            assertEquals("1", build[1].previousBuild?.name)
            assertEquals("3", build[1].nextBuild?.name)
            // Build 3 has no next build
            assertEquals("2", build[2].previousBuild?.name)
            assertNull(build[2].nextBuild)
        }
    }
}