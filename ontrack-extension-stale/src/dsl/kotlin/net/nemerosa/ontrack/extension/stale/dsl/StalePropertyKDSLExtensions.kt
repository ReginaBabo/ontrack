package net.nemerosa.ontrack.extension.stale.dsl

import net.nemerosa.ontrack.dsl.Project
import net.nemerosa.ontrack.dsl.property

/**
 * Representation of the stale property
 */
data class StaleProperty(
        val disablingDuration: Int = 0,
        val deletingDuration: Int = 0,
        val promotionsToKeep: List<String> = emptyList()
) {
    init {
        assert(disablingDuration >= 0) { "The disabling duration must be >= 0" }
        assert(deletingDuration >= 0) { "The deleting duration must be >= 0" }
    }
}

/**
 * Sets the stale property on a project.
 *
 * Sets the disabling and deleting durations (in days) on the project.
 */
fun Project.staleProperty(value: StaleProperty) {
    setProperty(
            "net.nemerosa.ontrack.extension.stale.StalePropertyType",
            value
    )
}

/**
 * Gets the stale property
 */
val Project.staleProperty: StaleProperty?
    get() = property("net.nemerosa.ontrack.extension.stale.StalePropertyType")
