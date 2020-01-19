package net.nemerosa.ontrack.dsl

/**
 * Management of labels
 */
interface Labels {
    /**
     * Creating a global label
     */
    fun createLabel(category: String?, name: String, description: String = "", color: String = "#000000"): Label

    /**
     * List of labels
     */
    val list: List<Label>

    /**
     * Creating or updating a global label
     */
    fun label(category: String?, name: String, description: String = "", color: String = "#000000"): Label

    /**
     * Gets a label by category and name
     */
    fun findLabel(category: String?, name: String): Label?

}

class LabelNotFoundException(category: String?, name: String) : DSLException(
        """Cannot find label: $category:$name"""
)

class LabelCannotChangeCategoryException(category: String?, name: String, newCategory: String?) : DSLException(
        """Cannot change a category for an existing label ($category:$name) to ($newCategory:$name)"""
)

/**
 * Label
 */
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
data class LabelProviderDescription(
        val id: String,
        val name: String
)
