package net.nemerosa.ontrack.kdsl.core.support

import com.fasterxml.jackson.databind.JsonNode
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JsonPropertyTest {

    @Test
    fun jsonText() {
        val json = mapOf(
                "name" to "My name",
                "description" to "My description"
        ).toJson()
        val test = TestClass(json)
        assertEquals("My name", test.name)
        assertEquals("My description", test.description)
    }

    @Test
    fun jsonOptionalText() {
        val test = TestOptionalText(mapOf(
                "name" to "My name",
                "description" to "My description"
        ).toJson())
        assertEquals("My name", test.name)
        assertEquals("My description", test.description)
        val test2 = TestOptionalText(mapOf(
                "name" to "My name"
        ).toJson())
        assertEquals("My name", test2.name)
        assertNull(test2.description)
    }

    private class TestClass(json: JsonNode) {
        val name: String by jsonText(json)
        val description: String by jsonText(json)
    }

    private class TestOptionalText(json: JsonNode) {
        val name: String by jsonText(json)
        val description: String? by jsonOptionalText(json)
    }

}