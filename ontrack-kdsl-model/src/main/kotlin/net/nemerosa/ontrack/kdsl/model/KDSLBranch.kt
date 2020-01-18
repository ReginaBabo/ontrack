package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Branch
import net.nemerosa.ontrack.dsl.Project
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.model.support.id
import net.nemerosa.ontrack.kdsl.model.support.name

class KDSLBranch(
        json: JsonNode,
        ontrackConnector: OntrackConnector
) : KDSLProjectEntity(json, ontrackConnector), Branch {

    override val entityType: String = "BRANCH"

    override val name: String = json.name

    override val project: Project by lazy {
        ontrack.getProjectByID(json["project"].id)
    }

}