package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.model.ProjectEntity

interface ValidationStamp : ProjectEntity {
    val name: String
    val description: String
}
