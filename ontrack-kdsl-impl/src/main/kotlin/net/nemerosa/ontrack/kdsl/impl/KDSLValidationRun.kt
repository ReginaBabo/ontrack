package net.nemerosa.ontrack.kdsl.impl

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.model.ValidationRun
import net.nemerosa.ontrack.kdsl.client.OntrackConnector

class KDSLValidationRun(json: JsonNode, ontrackConnector: OntrackConnector) : KDSLProjectEntity(json, ontrackConnector), ValidationRun {

    override val entityType: String = "VALIDATION_RUN"

}