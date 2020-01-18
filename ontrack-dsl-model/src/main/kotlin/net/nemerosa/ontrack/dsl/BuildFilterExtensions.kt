package net.nemerosa.ontrack.dsl

/**
 * Gets the last promoted builds for a branch
 */
val Branch.lastPromotedBuilds: List<Build>
    get() = filter("net.nemerosa.ontrack.service.PromotionLevelBuildFilterProvider", emptyMap())
