package net.nemerosa.ontrack.extension.stale.dsl

import net.nemerosa.ontrack.kdsl.model.Project

/**
 * Representation of the stale property
 */
class StaleProperty(
        val disablingDuration: Int,
        val deletingDuration: Int,
        val promotionsToKeep: List<String>
)

/**
 * Sets the stale property on a project.
 *
 * Sets the disabling and deleting durations (in days) on the project.
 */
fun Project.staleProperty(
        disablingDuration: Int = 0,
        deletingDuration: Int = 0,
        promotionsToKeep: List<String> = emptyList()
) {
    assert(disablingDuration >= 0) { "The disabling duration must be >= 0" }
    assert(deletingDuration >= 0) { "The deleting duration must be >= 0" }
    setProperty(
            "net.nemerosa.ontrack.extension.stale.StalePropertyType",
            StaleProperty(
                    disablingDuration = disablingDuration,
                    deletingDuration = deletingDuration,
                    promotionsToKeep = promotionsToKeep
            )
    )
}

/**
 * Gets the stale property
 */
val Project.staleProperty: StaleProperty?
    get() = getProperty<StaleProperty>("net.nemerosa.ontrack.extension.stale.StalePropertyType")
