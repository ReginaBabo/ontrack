package net.nemerosa.ontrack.kdsl.model

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
data class Label(
        val id: Int,
        val category: String?,
        val name: String,
        val description: String?,
        val color: String
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
 * List of labels
 */
val Labels.list: List<Label>
    get() = """
        labels {
            id
            category
            name
            description
            color
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
                id
                category
                name
                description
                color
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
 * Gets list of labels for a project
 */
val Project.labels: List<Label>
    get() =
        """
            projects(id: ${"$"}) {
                labels {
                    id
                    category
                    name
                    description
                    color
                }
            }
        """.graphQLQuery("Labels", "id" type "Int" value id)
                .data["projects"].map { it.toObject<Label>() }

/**
 * Thrown when a label cannot be found
 */
class LabelNotFoundException(category: String?, name: String) : DSLException(
        """Cannot find label: $category:$name"""
)
