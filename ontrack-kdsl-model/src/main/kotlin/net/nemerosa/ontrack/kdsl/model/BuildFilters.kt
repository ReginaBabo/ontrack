package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.model.support.query

/**
 * Gets the last promoted builds for a branch
 */
val Branch.lastPromotedBuilds: List<Build>
    get() = filter("net.nemerosa.ontrack.service.PromotionLevelBuildFilterProvider", emptyMap<String, Any>())

/**
 * Interval filter
 */
fun Branch.intervalFilter(
        from: String,
        to: String
): List<Build> = filter(
        "net.nemerosa.ontrack.service.BuildIntervalFilterProvider",
        mapOf(
                "from" to from,
                "to" to to
        )
)

/**
 * Standard filter
 *
 * TODO Add missing parameters
 */
fun Branch.standardFilter(
        withPromotionLevel: String? = null,
        withValidationStamp: String? = null,
        withValidationStampStatus: String? = null,
        sinceValidationStamp: String? = null,
        sinceValidationStampStatus: String? = null
): List<Build> = filter(
        "net.nemerosa.ontrack.service.StandardBuildFilterProvider",
        query(
                "withPromotionLevel" to withPromotionLevel,
                "withValidationStamp" to withValidationStamp,
                "withValidationStampStatus" to withValidationStampStatus,
                "sinceValidationStamp" to sinceValidationStamp,
                "sinceValidationStampStatus" to sinceValidationStampStatus
        )
)

/**
 * Runs any filter and returns the list of corresponding builds.
 */
fun Branch.filter(
        filterType: String,
        filterConfig: Map<String, Any>
): List<Build> {
    val url = "structure/branches/$id/view/$filterType"
    val json = ontrackConnector.get(
            url,
            filterConfig
    )
    return json
            ?.get("buildViews")
            ?.map { buildView ->
                buildView["build"].adaptSignature().toConnector<Build>()
            }
            ?: emptyList()
}