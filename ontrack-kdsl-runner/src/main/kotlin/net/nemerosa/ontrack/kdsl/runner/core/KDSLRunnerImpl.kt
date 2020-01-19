package net.nemerosa.ontrack.kdsl.runner.core

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.model.Ontrack
import net.nemerosa.ontrack.kdsl.runner.KDSLRunner

class KDSLRunnerImpl(ontrack: Ontrack) : KDSLRunner {

    override fun kdsl(script: String, bindings: Map<String, Any?>): JsonNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}