package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.dsl.*
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.*
import net.nemerosa.ontrack.kdsl.core.support.description
import net.nemerosa.ontrack.kdsl.core.support.name
import net.nemerosa.ontrack.kdsl.core.support.resources
import net.nemerosa.ontrack.kdsl.model.KDSLLabels.Companion.GRAPHQL_LABEL
import net.nemerosa.ontrack.kdsl.model.support.query

class KDSLProject(
        json: JsonNode,
        ontrackConnector: OntrackConnector
) : KDSLProjectEntity(json, ontrackConnector), Project {

    override val entityType: String = "PROJECT"

    override val name: String = json.name
    override val description: String = json.description

    override fun assignLabel(category: String?, name: String, createIfMissing: Boolean) {
        val existingLabel = ontrack.labels.findLabel(category, name)
        val label = when {
            existingLabel != null -> existingLabel
            createIfMissing -> ontrack.labels.createLabel(category, name)
            else -> throw LabelNotFoundException(category, name)
        }
        ontrackConnector.put(
                "/rest/labels/projects/${id}/assign/${label.id}",
                emptyMap<String, String>()
        )
    }

    override fun unassignLabel(category: String?, name: String, createIfMissing: Boolean) {
        val label = ontrack.labels.findLabel(category, name)
        if (label != null) {
            ontrackConnector.put(
                    "/rest/labels/projects/$id/unassign/${label.id}",
                    emptyMap<String, String>()
            )
        }
    }

    override val labels: List<Label>
        get() =
            graphQLQuery(
                    "ProjectLabels",
                    """
                        projects(id: ${"$"}id) {
                            labels {
                                $GRAPHQL_LABEL
                            }
                        }
                    """,
                    "id" type "Int" value id
            ).data["projects"][0]["labels"].map { KDSLLabel(it, ontrackConnector) }

    override val branches: List<Branch>
        get() = getResources("structure/projects/$id/branches") {
            KDSLBranch(it, ontrackConnector)
        }

    override fun createBranch(
            name: String,
            description: String,
            disabled: Boolean
    ): Branch =
            postAndConvert(
                    "structure/projects/$id/branches/create",
                    mapOf(
                            "name" to name,
                            "description" to description,
                            "disabled" to disabled
                    )
            ) { KDSLBranch(it, ontrackConnector) }

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
            KDSLBuild(buildView["build"], ontrackConnector)
        } ?: emptyList()
    }
}