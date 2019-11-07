package net.nemerosa.ontrack.bdd.model

import net.nemerosa.ontrack.kdsl.client.OntrackConnectorProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.annotation.PostConstruct

@ConfigurationProperties("bdd.model")
class BDDProperties {

    private val logger = LoggerFactory.getLogger(BDDProperties::class.java)

    var ontrack = OntrackProperties()

    class OntrackProperties : OntrackConnectorProperties {
        override var uri = "http://localhost:8080"
        override var username = "admin"
        override var password = "admin"
    }

    @PostConstruct
    fun logging() {
        logger.info("[BDD][Properties] Ontrack URL             = ${ontrack.uri}")
        logger.info("[BDD][Properties] Ontrack Username        = ${ontrack.username}")
        logger.info("[BDD][Properties] Ontrack Password        = *****")
    }

}