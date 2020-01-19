package net.nemerosa.ontrack.dsl.admin

import net.nemerosa.ontrack.dsl.Entity

interface AccountGroup : Entity {
    val name: String
    val description: String

    /**
     * Sets global permissions on an account group
     *
     * @param role Role to associate to the group
     */
    fun setGlobalPermission(role: String)

}