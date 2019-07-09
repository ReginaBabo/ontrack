package net.nemerosa.ontrack.boot.resources

import net.nemerosa.ontrack.json.ObjectMapperFactory
import net.nemerosa.ontrack.model.structure.NameDescription
import net.nemerosa.ontrack.model.structure.NameDescriptionState
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test

class LombokJsonDeserializationTest {

    @Test
    fun testJacksonAbleToDeserialize1() {
        val instance = NameDescription.nd("name", "description")
        val mapper = ObjectMapperFactory.create()
        val json = mapper.writeValueAsString(instance)
        val output = mapper.readValue(json, NameDescription::class.java)
        Assert.assertThat(output, CoreMatchers.equalTo(instance))
    }

    @Test
    fun testJacksonAbleToDeserialize2() {
        val instance = NameDescriptionState("name", "description", true)
        val mapper = ObjectMapperFactory.create()
        val json = mapper.writeValueAsString(instance)
        val output = mapper.readValue(json, NameDescriptionState::class.java)
        Assert.assertThat(output, CoreMatchers.equalTo(instance))
    }
}