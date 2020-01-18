package net.nemerosa.ontrack.dsl

/**
 * Gets the last promoted builds for a branch
 */
val Branch.lastPromotedBuilds: List<Build>
    get() = filter("net.nemerosa.ontrack.service.PromotionLevelBuildFilterProvider", emptyMap())

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
        toMapNotNull(
                "withPromotionLevel" to withPromotionLevel,
                "withValidationStamp" to withValidationStamp,
                "withValidationStampStatus" to withValidationStampStatus,
                "sinceValidationStamp" to sinceValidationStamp,
                "sinceValidationStampStatus" to sinceValidationStampStatus
        )
)
