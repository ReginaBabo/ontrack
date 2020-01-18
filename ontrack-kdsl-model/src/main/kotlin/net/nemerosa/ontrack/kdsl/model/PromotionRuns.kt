package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode

/**
 * Promotion run entity
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class PromotionRun(
        id: Int,
        creation: Signature,
        val promotionLevelId: Int
) : ProjectEntityResource(id, creation) {

    override val entityType: String = "PROMOTION_RUN"

    /**
     * Gets the promotion level for a promotion run
     */
    val promotionLevel: PromotionLevel by lazy {
        ontrack.getPromotionLevelByID(promotionLevelId)
    }

    /**
     * Deleting this run
     */
    fun delete() = ontrackConnector.delete("structure/promotionRuns/$id")

}

/**
 * REST adaptation
 */
private fun JsonNode?.toPromotionRun(): JsonNode? =
        adaptSignature()
                .adaptProjectId("build.branch.project")
                .adapt("promotionLevelId", "promotionLevel.id")

/**
 * Promotes a build.
 *
 * @receiver Build to promote
 * @param promotionLevel Name of the promotion level to promote to
 * @return Promotion run
 */
fun Build.promote(promotionLevel: String, description: String = "") =
        ontrackConnector.post(
                "structure/builds/$id/promotionRun/create",
                mapOf(
                        "promotionLevelName" to promotionLevel,
                        "description" to description
                )
        ).toPromotionRun().toConnector<PromotionRun>()

/**
 * List of promotion runs for a build
 */
val Build.promotionRuns: List<PromotionRun>
    get() = ontrackConnector.get("structure/builds/$id/promotionRun")
            ?.get("resources")
            ?.map {
                it.toPromotionRun().toConnector<PromotionRun>()
            }
            ?: emptyList()
