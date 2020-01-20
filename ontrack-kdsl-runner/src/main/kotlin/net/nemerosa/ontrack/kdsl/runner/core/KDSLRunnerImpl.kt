package net.nemerosa.ontrack.kdsl.runner.core

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.nemerosa.ontrack.kdsl.model.Ontrack
import net.nemerosa.ontrack.kdsl.runner.KDSLRunner
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class KDSLRunnerImpl(private val ontrack: Ontrack) : KDSLRunner {

    override fun kdsl(script: String, bindings: Map<String, Any?>): JsonNode {
        // Gets the script engine
        val engine: ScriptEngine = ScriptEngineManager().getEngineByName("kotlin")
                ?: throw IllegalStateException("Cannot find kotlin script engine")
        // Bindings
        val engineBindings = engine.createBindings()
        engineBindings.putAll(bindings)
        engineBindings["ontrack"] = ontrack
        // Evaluation
        val result = engine.eval(script, engineBindings)
        // Result transformation if needed
        return if (result is JsonNode) {
            result
        } else {
            ObjectMapper().valueToTree<JsonNode>(result)
        }
    }

}