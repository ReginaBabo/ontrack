package net.nemerosa.ontrack.bdd

import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct

class BDDProperties {

    private val logger = LoggerFactory.getLogger(BDDProperties::class.java)

    var ontrack = OntrackProperties()

    class OntrackProperties {
        var url = "http://localhost:8080"
        var username = "admin"
        var password = "admin"
    }

    @PostConstruct
    fun logging() {
        logger.info("[BDD][Properties] Ontrack URL             = ${ontrack.url}")
        logger.info("[BDD][Properties] Ontrack Username        = ${ontrack.username}")
        logger.info("[BDD][Properties] Ontrack Password        = *****")
    }

}