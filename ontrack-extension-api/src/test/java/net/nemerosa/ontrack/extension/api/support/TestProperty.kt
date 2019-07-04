package net.nemerosa.ontrack.extension.api.support

import net.nemerosa.ontrack.model.support.ConfigurationProperty

class TestProperty(
        private val configuration: TestConfiguration,
        val value: String?
) : ConfigurationProperty<TestConfiguration> {

    override fun getConfiguration(): TestConfiguration = configuration

    companion object {

        @JvmStatic
        fun of(value: String): TestProperty {
            return of(
                    TestConfiguration.config("test"),
                    value
            )
        }

        @JvmStatic
        fun of(configuration: TestConfiguration, value: String): TestProperty {
            return TestProperty(
                    configuration,
                    value
            )
        }
    }
}
