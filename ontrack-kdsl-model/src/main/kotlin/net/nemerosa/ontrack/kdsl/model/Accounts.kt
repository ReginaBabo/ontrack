package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import net.nemerosa.ontrack.kdsl.core.Ontrack
import net.nemerosa.ontrack.kdsl.core.Resource

/**
 * Account group
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AccountGroup(
        id: Int,
        val name: String,
        val description: String
) : EntityResource(id)

/**
 * Account DSL extension
 */
class Accounts : Resource() {

    /**
     * List of account groups
     */
    val accountGroups: List<AccountGroup>
        get() = """
            accountGroups {
                id
                name
                description
            }
        """.trimIndent().graphQLQuery("AccountGroups")
                .data["accountGroups"]
                .map { it.toConnector<AccountGroup>() }

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
    ): AccountGroup =
            accountGroups.find { it.name == name } ?: createAccountGroup(name, description)

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
