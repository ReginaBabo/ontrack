package net.nemerosa.ontrack.kdsl.model.admin

/**
 * Creates or gets a group
 */
fun AccountManagement.accountGroup(
        name: String,
        description: String = ""
): AccountGroup = findAccountGroupByName(name) ?: createAccountGroup(name, description)
