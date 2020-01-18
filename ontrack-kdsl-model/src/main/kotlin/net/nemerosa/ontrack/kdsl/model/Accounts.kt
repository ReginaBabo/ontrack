package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.kdsl.core.Ontrack
import net.nemerosa.ontrack.kdsl.core.Resource

/**
 * Account
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Account(
        val id: Int,
        val name: String,
        val fullName: String,
        val email: String
) : Resource()

/**
 * Account group
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AccountGroup(
        json: JsonNode,
        id: Int,
        val name: String,
        val description: String
) : EntityResource(json, id)

/**
 * Account DSL extension
 */
class Accounts : Resource() {

    /**
     * List of account groups
     */
    val accountGroups: List<AccountGroup>
        get() = ontrackConnector.get("accounts/groups")
                ?.get("resources")
                ?.map { it.toConnector<AccountGroup>() }
                ?: emptyList()

    /**
     * Find an account group by name
     *
     * @param name Name of the group to find
     * @return Group if found
     */
    fun findAccountGroupByName(name: String) = accountGroups.find { it.name == name }

    /**
     * Creates a group
     */
    fun createAccountGroup(
            name: String,
            description: String = ""
    ): AccountGroup =
            ontrackConnector.post(
                    "accounts/groups/create",
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            ).toConnector()

    /**
     * Creates or gets a group
     */
    fun accountGroup(
            name: String,
            description: String = ""
    ): AccountGroup = findAccountGroupByName(name) ?: createAccountGroup(name, description)

    /**
     * Creating an account
     *
     * @param name Unique name for the account
     * @param fullName Display name for the account
     * @param email Email for the account
     * @param password Password the account
     * @param groupNames Groups the account belongs to
     */
    fun createAccount(
            name: String,
            fullName: String,
            email: String,
            password: String,
            groupNames: List<String> = emptyList()
    ): Account {
        // Loading the groups
        val groups = groupNames.mapNotNull { findAccountGroupByName(it) }
        // Account creation
        return ontrackConnector.post(
                "accounts/create",
                mapOf(
                        "name" to name,
                        "fullName" to fullName,
                        "email" to email,
                        "password" to password,
                        "groups" to groups.map { it.id }
                )
        ).toConnector()
    }
}

/**
 * Extension point
 */
val Ontrack.accounts: Accounts
    get() = Accounts().apply { init(this@accounts) }
