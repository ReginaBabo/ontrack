package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.Branch
import net.nemerosa.ontrack.dsl.Build
import net.nemerosa.ontrack.dsl.Project
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.type
import net.nemerosa.ontrack.kdsl.core.value
import net.nemerosa.ontrack.kdsl.model.support.description
import net.nemerosa.ontrack.kdsl.model.support.name
import net.nemerosa.ontrack.kdsl.model.support.query
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

    override fun searchBuilds(
            maximumCount: Int,
            branchName: String?,
            buildName: String?,
            buildExactMatch: Boolean,
            promotionName: String?,
            validationStampName: String?,
            property: String?,
            propertyValue: String?,
            linkedFrom: String?,
            linkedTo: String?
    ): List<Build> {
        val url = "structure/project/$id/builds/search"
        // Form
        val query = query(
                "maximumCount" to maximumCount,
                "branchName" to branchName,
                "buildName" to buildName,
                "buildExactMatch" to buildExactMatch,
                "promotionName" to promotionName,
                "validationStampName" to validationStampName,
                "property" to property,
                "propertyValue" to propertyValue,
                "linkedFrom" to linkedFrom,
                "linkedTo" to linkedTo
        )
        // Query
        return ontrackConnector.get(url, query)?.resources?.map { buildView ->
            buildView["build"].let { KDSLBuild(it, ontrackConnector) }
        } ?: emptyList()
    }
}