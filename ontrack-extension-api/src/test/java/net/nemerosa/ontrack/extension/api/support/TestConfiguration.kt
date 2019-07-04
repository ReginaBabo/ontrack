package net.nemerosa.ontrack.extension.api.support

import net.nemerosa.ontrack.model.support.ConfigurationDescriptor
import net.nemerosa.ontrack.model.support.UserPasswordConfiguration
import java.util.function.Function

data class TestConfiguration(
        private val name: String,
        private val user: String? = null,
        private val password: String? = null
) : UserPasswordConfiguration<TestConfiguration> {

    override fun getName(): String = name

    override fun getUser(): String? = user

    override fun getPassword(): String? = password

    override fun withPassword(password: String?): TestConfiguration {
        return TestConfiguration(
                name,
                user,
                password
        )
    }

    override fun clone(targetConfigurationName: String, replacementFunction: Function<String, String>): TestConfiguration {
        return TestConfiguration(
                targetConfigurationName,
                user,
                password
        )
    }

    override fun getDescriptor(): ConfigurationDescriptor {
        return ConfigurationDescriptor("test", name)
    }

    override fun obfuscate(): TestConfiguration {
        return TestConfiguration(
                name,
                user,
                ""
        )
    }

    companion object {

        const val PLAIN_PASSWORD = "verysecret"

        @JvmStatic
        fun config(name: String): TestConfiguration {
            return TestConfiguration(name, "user", PLAIN_PASSWORD)
        }
    }
}
