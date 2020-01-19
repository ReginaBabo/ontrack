package net.nemerosa.ontrack.kdsl.impl

import com.nhaarman.mockitokotlin2.mock
import net.nemerosa.ontrack.kdsl.core.support.toJson
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KDSLLabelTest {

    @Test
    fun `Label parsing`() {
        val json = mapOf(
                "id" to 10,
                "category" to "CAT",
                "name" to "Name",
                "description" to "Some description",
                "color" to "#FFFFFF",
                "computedBy" to mapOf(
                        "id" to "ID",
                        "name" to "NAME"
                ),
                "foregroundColor" to "#000000",
                "display" to "Some fancy name"
        ).toJson()
        val label = KDSLLabel(json, mock())
        assertNotNull(label.computedBy) {
            assertEquals("ID", it.id)
            assertEquals("NAME", it.name)
        }
    }

    @Test
    fun `Label parsing with null computed by`() {
        val json = mapOf(
                "id" to 10,
                "category" to "CAT",
                "name" to "Name",
                "description" to "Some description",
                "color" to "#FFFFFF",
                "computedBy" to null,
                "foregroundColor" to "#000000",
                "display" to "Some fancy name"
        ).toJson()
        val label = KDSLLabel(json, mock())
        assertNull(label.computedBy)
    }

}