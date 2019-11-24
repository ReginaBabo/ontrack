package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import net.nemerosa.ontrack.kdsl.core.Ontrack

/**
 * Promotion level entity
 *
 * @property name Name of this promotion level
 * @property description Description of this promotion level
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class PromotionLevel(
        id: Int,
        @JsonProperty
        override val projectId: Int,
        creation: Signature,
        val name: String,
        val description: String
) : ProjectEntityResource(id, creation) {

    override val entityType: String = "PROMOTION_LEVEL"

}

/**
 * Gets a promotion level by ID
 */
fun Ontrack.getPromotionLevelByID(id: Int) =
        ontrackConnector.get("structure/promotionLevels/$id")
                ?.adaptProjectId("branch.project")
                ?.adaptSignature()
                ?.toConnector<PromotionLevel>()
                ?: throw EntityNotFoundException("promotion level", id)

/**
 * List of promotion levels for a branch.
 */
val Branch.promotionLevels: List<PromotionLevel>
    get() =
        """
            branches(id: ${'$'}id) {
                promotionLevels {
                    id
                    name
                    description
                    creation {
                        user
                        time
                    }
                }
            }
        """.trimIndent().graphQLQuery(
                "PromotionLevels",
                "id" type "Int!" value this.id
        ).data["branches"][0]["promotionLevels"].map { it.adaptSignature().toConnector<PromotionLevel>() }


/**
 * Creates a promotion level.
 *
 * @param name Name of the promotion level
 * @param description Description of the promotion level
 * @return Created promotion level
 */
fun Branch.createPromotionLevel(
        name: String,
        description: String = ""
): PromotionLevel =
        ontrackConnector.post(
                "structure/branches/$id/promotionLevels/create",
                mapOf(
                        "name" to name,
                        "description" to description
                )
        ).adaptSignature().toConnector()

/**
 * Creates or gets a promotion level and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the promotion level
 * @param description Description of the promotion level
 * @param initFn Code to run against the created promotion level
 * @return Object return by [initFn]
 */
fun <T> Branch.promotionLevel(
        name: String,
        description: String = "",
        initFn: PromotionLevel.() -> T
): T {
    val pl = promotionLevels.firstOrNull { it.name == name } ?: createPromotionLevel(name, description)
    return pl.initFn()
}

/**
 * Creates or gets a promotion level.
 *
 * @param name Name of the promotion level
 * @param description Description of the promotion level
 * @return Created or retrieved promotion level
 */
fun Branch.promotionLevel(
        name: String,
        description: String = ""
): PromotionLevel = promotionLevel(name, description) { this }
