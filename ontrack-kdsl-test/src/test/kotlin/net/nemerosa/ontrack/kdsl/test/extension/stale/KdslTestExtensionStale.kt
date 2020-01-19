package net.nemerosa.ontrack.kdsl.test.extension.stale

import net.nemerosa.ontrack.kdsl.model.project
import net.nemerosa.ontrack.extension.stale.dsl.StaleProperty
import net.nemerosa.ontrack.extension.stale.dsl.staleProperty
import net.nemerosa.ontrack.kdsl.test.app.SpringTest
import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
import net.nemerosa.ontrack.test.support.uid
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringTest
class KdslTestExtensionStale : AbstractKdslTest() {

    @Test
    fun `Stale property`() {
        val projectName = uid("P")
        ontrack.project(projectName) {
            assertNull(staleProperty, "Stale property on project is not defined")
            staleProperty(StaleProperty(
                    disablingDuration = 15,
                    deletingDuration = 30,
                    promotionsToKeep = emptyList()
            ))
            assertNotNull(staleProperty, "Stale property is defined") {
                assertEquals(15, it.disablingDuration)
                assertEquals(30, it.deletingDuration)
                assertEquals(emptyList(), it.promotionsToKeep)
            }
        }
    }

    @Test
    fun `Stale property with promotions to keep`() {
        val projectName = uid("P")
        ontrack.project(projectName) {
            staleProperty(StaleProperty(
                    disablingDuration = 15,
                    deletingDuration = 30,
                    promotionsToKeep = listOf("DELIVERY", "PROMOTION")
            ))
            assertNotNull(staleProperty, "Stale property is defined") {
                assertEquals(15, it.disablingDuration)
                assertEquals(30, it.deletingDuration)
                assertEquals(listOf("DELIVERY", "PROMOTION"), it.promotionsToKeep)
            }
        }
    }

}