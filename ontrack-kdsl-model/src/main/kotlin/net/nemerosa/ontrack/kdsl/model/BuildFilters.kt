package net.nemerosa.ontrack.kdsl.model

/**
 * Gets the last promoted builds for a branch
 */
val Branch.lastPromotedBuilds: List<Build>
    get() = filter("net.nemerosa.ontrack.service.PromotionLevelBuildFilterProvider", emptyMap<String, Any>())


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
                buildView["build"].toConnector<Build>()
            }
            ?: emptyList()
}