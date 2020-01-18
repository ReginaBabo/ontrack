package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import net.nemerosa.ontrack.kdsl.core.deepPath
import net.nemerosa.ontrack.kdsl.core.parseInto
import net.nemerosa.ontrack.kdsl.core.removeDeepPath
import net.nemerosa.ontrack.kdsl.core.support.DSLException
import net.nemerosa.ontrack.kdsl.core.toJson
import kotlin.reflect.KClass

/**
 * Entity resource linked to a project
 *
 * @property signature Creation time and author
 */
abstract class ProjectEntityResource(
        json: JsonNode,
        id: Int,
        val signature: Signature
) : EntityResource(json, id) {

    /**
     * Entity type
     */
    abstract val entityType: String

    /**
     * Sets a property on this entity
     */
    fun setProperty(type: String, value: Any) {
        ontrackConnector.put(
                "properties/$entityType/$id/$type/edit",
                value
        )
    }

    /**
     * Gets a property on this entity
     */
    inline fun <reified T : Any> getProperty(type: String): T? =
            getProperty(T::class, type)

    /**
     * Gets a property on this entity
     */
    fun <T : Any> getProperty(kClass: KClass<T>, type: String): T? {
        return ontrackConnector.get(
                "properties/$entityType/$id/$type/view"
        )?.get("value")?.parseInto(kClass)
    }

}

@Deprecated("Don't use it.")
fun JsonNode?.adaptProjectId(projectPath: String = "project"): JsonNode? = adapt(
        "projectId",
        "${projectPath}.id"
)

@Deprecated("Don't use it.")
fun JsonNode?.adapt(newField: String, oldField: String): JsonNode? {
    return if (this is ObjectNode) {
        val oldNode = deepPath(oldField)
        when {
            oldNode.isMissingNode || oldNode.isNull -> throw NoFieldException(oldField)
            else -> {
                this.removeDeepPath(oldField)
                this.set(newField, oldNode)
            }
        }
    } else {
        this
    }
}

@Deprecated("Don't use it.")
fun JsonNode?.adaptSignature(): JsonNode? {
    return when (this) {
        is ObjectNode -> {
            val signatureNode = get("signature")
            when {
                signatureNode == null -> this
                signatureNode.isNull -> this
                else -> {
                    remove("signature")
                    val time = signatureNode.path("time").asText()
                    val user = signatureNode.path("user").path("name").asText()
                    set("creation", mapOf(
                            "time" to time,
                            "user" to user
                    ).toJson())
                }
            }
        }
        else -> this
    }
}

class NoFieldException(path: String) : DSLException("No path $path found in JSON node")
