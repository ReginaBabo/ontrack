package net.nemerosa.ontrack.kdsl.client.support

import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.client.OntrackConnectorProperties
import net.nemerosa.ontrack.kdsl.client.impl.HttpOntrackConnector

object OntrackConnectorBuilder {

    var ontrackConnector: OntrackConnector? = null

    fun getOrCreateFromEnv(ontrackConnectorProperties: OntrackConnectorProperties?): OntrackConnector =
            ontrackConnector ?: createFromEnv(ontrackConnectorProperties).apply { ontrackConnector = this }

    private fun createFromEnv(ontrackConnectorProperties: OntrackConnectorProperties?): OntrackConnector {
        val actualProperties: OntrackConnectorProperties = ontrackConnectorProperties ?: getOrCreatePropertiesFromEnv()
        return HttpOntrackConnector(
                url = actualProperties.uri,
                username = actualProperties.username,
                password = actualProperties.password
        )
    }

    private fun getOrCreatePropertiesFromEnv() = SystemOntrackConnectorProperties()

    private class SystemOntrackConnectorProperties : OntrackConnectorProperties {
        override val uri: String
            get() = getPropertyOrEnv("bdd.model.ontrack.uri", "http://localhost:8080")
        override val username: String?
            get() = getPropertyOrEnv("bdd.model.ontrack.username", "admin")
        override val password: String?
            get() = getPropertyOrEnv("bdd.model.ontrack.password", "admin")

        private fun getPropertyOrEnv(key: String, defaultValue: String) =
                getProperty(key) ?: getEnv(key) ?: defaultValue

        private fun getEnv(key: String): String? = System.getenv(key.toUpperCase().replace(".", "_"))

        private fun getProperty(key: String): String? = System.getProperty(key, null)

    }


}