package net.nemerosa.ontrack.kdsl.runner

import com.fasterxml.jackson.databind.JsonNode

interface KDSLRunner {

    /**
     * Runs a KDSL script.
     *
     * @param script Script to run
     * @param bindings Bindings to the script
     */
    fun kdsl(script: String, bindings: Map<String, Any?>): JsonNode

}