package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.dsl.Label
import net.nemerosa.ontrack.dsl.LabelCannotChangeCategoryException
import net.nemerosa.ontrack.dsl.LabelNotFoundException
import net.nemerosa.ontrack.dsl.Labels
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.Connector
import net.nemerosa.ontrack.kdsl.core.parse
import net.nemerosa.ontrack.kdsl.core.type
import net.nemerosa.ontrack.kdsl.core.value

class KDSLLabels(ontrackConnector: OntrackConnector) : Connector(ontrackConnector), Labels {

    override fun createLabel(category: String?, name: String, description: String, color: String): Label =
            postAndParseAsObject("rest/labels/create", mapOf(
                    "category" to category,
                    "name" to name,
                    "description" to description,
                    "color" to color
            ))

    override val list: List<Label>
        get() =
            graphQLQuery(
                    "LabelsList",
                    """
                        labels {
                            $GRAPHQL_LABEL
                        }
                    """
            ).data["labels"].map { it.parse<Label>() }

    override fun label(category: String?, name: String, description: String, color: String): Label {
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
            postAndParseAsObject("rest/labels/create", mapOf(
                    "category" to category,
                    "name" to name,
                    "description" to description,
                    "color" to color
            ))
        }
    }

    override fun findLabel(category: String?, name: String): Label? =
            graphQLQuery(
                    "FindLabel",
                    """
                        labels(category: ${'$'}category, name: ${'$'}name) {
                            $GRAPHQL_LABEL
                        }
                    """,
                    "category" type "String" value category,
                    "name" type "String!" value name
            ).data["labels"].firstOrNull()?.parse()

    companion object {
        /**
         * GraphQL label definition
         */
        const val GRAPHQL_LABEL = """
            id
            category
            name
            description
            color
            computedBy { id name }
            foregroundColor
            display
        """

    }

}
