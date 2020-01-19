package net.nemerosa.ontrack.kdsl.impl.admin

import net.nemerosa.ontrack.kdsl.model.admin.Account
import net.nemerosa.ontrack.kdsl.model.admin.AccountGroup
import net.nemerosa.ontrack.kdsl.model.admin.AccountManagement
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.Connector

class KDSLAccountManagement(ontrackConnector: OntrackConnector) : Connector(ontrackConnector), AccountManagement {

    override val accountGroups: List<AccountGroup>
        get() = getResources("accounts/groups") {
            KDSLAccountGroup(it, ontrackConnector)
        }

    override fun createAccountGroup(name: String, description: String): AccountGroup =
            postAndConvert(
                    "accounts/groups/create",
                    mapOf(
                            "name" to name,
                            "description" to description
                    )
            ) { KDSLAccountGroup(it, ontrackConnector) }

    override fun createAccount(
            name: String,
            fullName: String,
            email: String,
            password: String,
            groupNames: List<String>
    ): Account {
        // Loading the groups
        val groups = groupNames.mapNotNull { findAccountGroupByName(it) }
        // Account creation
        return postAndConvert(
                "accounts/create",
                mapOf(
                        "name" to name,
                        "fullName" to fullName,
                        "email" to email,
                        "password" to password,
                        "groups" to groups.map { it.id }
                )
        ) { KDSLAccount(it, ontrackConnector) }
    }

}
