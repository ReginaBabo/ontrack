package net.nemerosa.ontrack.graphql.schema

import net.nemerosa.ontrack.model.structure.BuildSearchForm
import org.junit.Test
import kotlin.test.assertEquals

class GQLInputBuildSearchFormTest {

    @Test
    fun `Null argument`() {
        val form = GQLInputBuildSearchForm().convert(null)
        assertEquals(BuildSearchForm().withMaximumCount(10), form)
    }

    @Test
    fun `Validation stamp name`() {
        val form = GQLInputBuildSearchForm().convert(mapOf("validationStampName" to "VS"))
        assertEquals(BuildSearchForm().withValidationStampName("VS"), form)
    }

    @Test
    fun `Build exact match`() {
        val form = GQLInputBuildSearchForm().convert(mapOf("buildName" to "1.0.0-123", "buildExactMatch" to true))
        assertEquals(BuildSearchForm().withBuildName("1.0.0-123").withBuildExactMatch(true), form)
    }

}
