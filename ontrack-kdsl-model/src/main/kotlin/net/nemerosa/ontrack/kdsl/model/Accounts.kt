package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.core.Connector
import net.nemerosa.ontrack.kdsl.core.Ontrack

/**
 * Account DSL extension
 */
class Accounts : Connector() {
    /**
     * Creates a group
     */
    fun accountGroup(
            name: String,
            description: String = ""
    ) {
        TODO("Gets or creates the group")
    }
}

/**
 * Extension point
 */
val Ontrack.accounts: Accounts
    get() = Accounts().apply { init(this@accounts) }
