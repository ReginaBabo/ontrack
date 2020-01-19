package net.nemerosa.ontrack.kdsl.model.admin

import net.nemerosa.ontrack.kdsl.model.Entity

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