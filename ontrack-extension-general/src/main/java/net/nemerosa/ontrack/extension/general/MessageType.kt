package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.model.structure.Describable

enum class MessageType(
        private val displayName: String,
        private val description: String
) : Describable {

    ERROR("Error", "Error message"),
    WARNING("Warning", "Warning message"),
    INFO("Info", "Information message");

    override fun getId(): String {
        return name
    }

    override fun getName(): String {
        return displayName
    }

    override fun getDescription(): String {
        return description
    }

}