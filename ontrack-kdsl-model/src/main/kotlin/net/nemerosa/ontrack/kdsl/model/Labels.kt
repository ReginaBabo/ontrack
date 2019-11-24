package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import net.nemerosa.ontrack.kdsl.core.Ontrack
import net.nemerosa.ontrack.kdsl.core.Resource
import net.nemerosa.ontrack.kdsl.core.support.DSLException

/**
 * Management of labels
 */
class Labels : Resource()

/**
 * Extension point
 */
val Ontrack.labels: Labels
    get() = Labels().apply { init(this@labels) }

/**
 * Label
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Label(
        val id: Int,
        val category: String?,
        val name: String,
        val description: String?,
        val color: String,
        val computedBy: LabelProviderDescription?,
        val foregroundColor: String,
        val display: String
)

/**
 * Descriptor
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class LabelProviderDescription(
        val id: String,
        val name: String
)

/**
 * Creating a global label
 */
fun Labels.createLabel(category: String?, name: String, description: String = "", color: String = "#000000"): Label =
        ontrackConnector.post("rest/labels/create", mapOf(
                "category" to category,
                "name" to name,
                "description" to description,
                "color" to color
        )).toObject()

/**
 * Creating or updating a global label
 */
fun Labels.label(category: String?, name: String, description: String = "", color: String = "#000000"): Label {
    val label = findLabel(category, name)
    return if (label != null) {
        if (label.category != category) {
            throw LabelCannotChangeCategoryException(label.category, label.name, category)
        } else {
            ontrackConnector.put("rest/labels/${label.id}/update", mapOf(
                    "category" to category,
                    "name" to name,
                    "description" to description,
                    "color" to color
            ))
            findLabel(category, name) ?: throw LabelNotFoundException(category, name)
        }
    } else {
        ontrackConnector.post("rest/labels/create", mapOf(
                "category" to category,
                "name" to name,
                "description" to description,
                "color" to color
        )).toObject()
    }
}

/**
 * List of labels
 */
val Labels.list: List<Label>
    get() = """
        labels {
            $GRAPHQL_LABEL
        }
    """.trimIndent().graphQLQuery("Labels")
            .data["labels"]
            .map { it.toObject<Label>() }

/**
 * Gets a label by category and name
 */
fun Labels.findLabel(category: String?, name: String): Label? =
        """
            labels(category: ${'$'}category, name: ${'$'}name) {
                $GRAPHQL_LABEL
            }
        """.trimIndent().graphQLQuery(
                "LabelSearch",
                "category" type "String" value category,
                "name" type "String!" value name
        ).data["labels"].firstOrNull()?.toObject()

/**
 * Assigning a label to a project
 */
fun Project.assignLabel(category: String?, name: String, createIfMissing: Boolean = false) {
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

/**
 * Unassigning a label to a project
 */
fun Project.unassignLabel(category: String?, name: String, createIfMissing: Boolean = false) {
    val label = ontrack.labels.findLabel(category, name)
    if (label != null) {
        ontrackConnector.put("/rest/labels/projects/$id/unassign/${label.id}", emptyMap<String, String>())
    }
}

/**
 * Gets list of labels for a project
 */
val Project.labels: List<Label>
    get() =
        """
            projects(id: ${"$"}id) {
                labels {
                    $GRAPHQL_LABEL
                }
            }
        """.graphQLQuery("Labels", "id" type "Int" value id)
                .data["projects"][0]["labels"].map { it.toObject<Label>() }

/**
 * Thrown when a label cannot be found
 */
class LabelNotFoundException(category: String?, name: String) : DSLException(
        """Cannot find label: $category:$name"""
)

class LabelCannotChangeCategoryException(category: String?, name: String, newCategory: String?) : DSLException(
        """Cannot change a category for an existing label ($category:$name) to ($newCategory:$name)"""
)

/**
 * GraphQL label definition
 */
private const val GRAPHQL_LABEL = """
    id
    category
    name
    description
    color
    computedBy { id name }
    foregroundColor
    display
"""
