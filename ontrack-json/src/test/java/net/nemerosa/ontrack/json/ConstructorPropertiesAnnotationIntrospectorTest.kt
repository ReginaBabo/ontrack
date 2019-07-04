package net.nemerosa.ontrack.json

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class ConstructorPropertiesAnnotationIntrospectorTest {

    private val instance = ImmutablePojo("foobar", 42)

    private data class ImmutablePojo(
            val name: String,
            val value: Int
    )

    @Test
    fun testJacksonAbleToDeserialize() {
        val mapper = ObjectMapperFactory.create()
        val json = mapper.writeValueAsString(instance)
        val output = mapper.readValue(json, ImmutablePojo::class.java)
        assertThat(output, equalTo(instance))
    }
}
