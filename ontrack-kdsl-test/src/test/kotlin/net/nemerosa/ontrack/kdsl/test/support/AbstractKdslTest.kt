package net.nemerosa.ontrack.kdsl.test.support

import net.nemerosa.ontrack.dsl.*
import net.nemerosa.ontrack.kdsl.model.KDSLOntrack
import net.nemerosa.ontrack.test.support.uid

abstract class AbstractKdslTest {

    protected val ontrack: Ontrack = KDSLOntrack.connect()

    /**
     * Shortcut for a project with a unique name
     */
    fun project(code: Project.() -> Unit) {
        val name = uid("P")
        ontrack.project(name) {
            code()
        }
    }

    /**
     * Shortcut for a creating a branch in a project
     */
    fun Project.branch(code: Branch.() -> Unit) {
        val name = uid("B")
        branch(name) {
            code()
        }
    }

    /**
     * Shortcut for a creating a branch
     */
    fun branch(code: Branch.() -> Unit) {
        project {
            branch {
                code()
            }
        }
    }

    /**
     * Switches to a mode when the "Grant view to all" is forcibly disabled
     */
    fun <T> withNotGrantProjectViewToAll(code: () -> T): T {
        val oldGrant = ontrack.settings.grantProjectViewToAll
        return try {
            ontrack.settings.grantProjectViewToAll = false
            // Action
            code()
        } finally {
            ontrack.settings.grantProjectViewToAll = oldGrant
        }
    }

}