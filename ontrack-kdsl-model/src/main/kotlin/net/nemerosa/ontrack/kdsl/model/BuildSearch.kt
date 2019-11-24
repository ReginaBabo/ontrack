package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.model.support.query

/**
 * Searching a build on a project
 */
fun Project.searchBuilds(
        maximumCount: Int = 10,
        branchName: String? = null,
        buildName: String? = null,
        buildExactMatch: Boolean = false,
        promotionName: String? = null,
        validationStampName: String? = null,
        property: String? = null,
        propertyValue: String? = null,
        linkedFrom: String? = null,
        linkedTo: String? = null
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
    return ontrackConnector.get(url, query)?.get("resources")?.map { buildView ->
        buildView["build"].adaptSignature().toConnector<Build>()
    } ?: emptyList()
}