package net.nemerosa.ontrack.kdsl.model.admin

interface AccountManagement {
    /**
     * List of account groups
     */
    val accountGroups: List<AccountGroup>

    /**
     * Find an account group by name
     *
     * @param name Name of the group to find
     * @return Group if found
     */
    fun findAccountGroupByName(name: String): AccountGroup? = accountGroups.find { it.name == name }

    /**
     * Creates a group
     */
    fun createAccountGroup(name: String, description: String = ""): AccountGroup

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
    ): Account

}