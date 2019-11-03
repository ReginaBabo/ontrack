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

    /**
     * Creating or updating an account
     *
     * @param name Unique name for the account
     * @param fullName Display name for the account (defaults to [name])
     * @param email Email for the account (defaults to "[name]@test.com)
     * @param password Password the account
     * @param groupNames Groups the account belongs to
     */
    fun account(
            name: String,
            fullName: String?,
            email: String?,
            password: String = "",
            groupNames: List<String> = emptyList()) {
        TODO("Creating or updating an account")
    }
}

/**
 * Extension point
 */
val Ontrack.accounts: Accounts
    get() = Accounts().apply { init(this@accounts) }
