package net.nemerosa.ontrack.kdsl.model

/**
 * Promotes a build.
 *
 * @receiver Build to promote
 * @param promotionLevel Name of the promotion level to promote to
 * TODO @return Promotion run
 */
fun Build.promote(promotionLevel: String, description: String = "") {
    ontrackConnector.post(
            "structure/builds/$id/promotionRun/create",
            mapOf(
                    "promotionLevelName" to promotionLevel,
                    "description" to description
            )
    )
}