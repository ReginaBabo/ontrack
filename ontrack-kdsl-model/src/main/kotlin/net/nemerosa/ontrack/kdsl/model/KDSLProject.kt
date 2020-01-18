package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Branch
import net.nemerosa.ontrack.dsl.Project
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.type
import net.nemerosa.ontrack.kdsl.core.value
import net.nemerosa.ontrack.kdsl.model.support.description
import net.nemerosa.ontrack.kdsl.model.support.name
import net.nemerosa.ontrack.kdsl.model.support.resources

class KDSLProject(
        json: JsonNode,
        ontrackConnector: OntrackConnector
) : KDSLProjectEntity(json, ontrackConnector), Project {

    override val entityType: String = "PROJECT"

    override val name: String = json.name
    override val description: String = json.description

    override val branches: List<Branch>
        get() = ontrackConnector.get("structure/projects/$id/branches")?.resources?.map {
            KDSLBranch(it, ontrackConnector)
        } ?: emptyList()

    override fun createBranch(
            name: String,
            description: String,
            disabled: Boolean
    ): Branch =
            ontrackConnector.post(
                    "structure/projects/$id/branches/create",
                    mapOf(
                            "name" to name,
                            "description" to description,
                            "disabled" to disabled
                    )
            )?.let {
                KDSLBranch(it, ontrackConnector)
            } ?: throw MissingResponseException()

    override fun branches(
            name: String
    ): List<Branch> = graphQLQuery(
            "Branches",
            """branches(name: ${'$'}name, project: ${'$'}project) { json }""",
            "name" type "String" value name,
            "project" type "String!" value this.name
    ).data["branches"].map {
        KDSLBranch(it["json"], ontrackConnector)
    }

}