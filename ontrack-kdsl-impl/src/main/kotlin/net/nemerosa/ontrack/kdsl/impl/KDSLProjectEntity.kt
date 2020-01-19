package net.nemerosa.ontrack.kdsl.impl

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.model.ProjectEntity
import net.nemerosa.ontrack.kdsl.model.Signature
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.support.parseInto
import net.nemerosa.ontrack.kdsl.impl.support.signature
import kotlin.reflect.KClass

abstract class KDSLProjectEntity(
        json: JsonNode,
        ontrackConnector: OntrackConnector
) : KDSLEntity(json, ontrackConnector), ProjectEntity {

    /**
     * Entity type
     */
    abstract val entityType: String

    override val signature: Signature by lazy { json.signature }

    override fun setProperty(type: String, value: Any) {
        ontrackConnector.put(
                "properties/$entityType/$id/$type/edit",
                value
        )
    }

    override fun <T : Any> getProperty(kClass: KClass<T>, type: String): T? =
            ontrackConnector.get(
                    "properties/$entityType/$id/$type/view"
            )?.get("value")?.parseInto(kClass)
}