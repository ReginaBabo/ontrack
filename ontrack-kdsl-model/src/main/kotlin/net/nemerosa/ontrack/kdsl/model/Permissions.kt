package net.nemerosa.ontrack.kdsl.model

/**
 * Sets global permissions on an account group
 *
 * @receiver The group for which to set a global permission
 * @param role Role to associate to the group
 */
fun AccountGroup.setGlobalPermission(
        role: String
) {
    ontrackConnector.put(
            "accounts/permissions/globals/GROUP/${id}",
            mapOf(
                    "role" to role
            )
    )
}
