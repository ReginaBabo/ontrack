package net.nemerosa.ontrack.kdsl.client

/**
 * Defines the connection properties to Ontrack.
 */
interface OntrackConnectorProperties {
    /**
     * URI to connect to
     */
    val uri: String
    /**
     * User name used for authentication (can be `null` for anonymous access)
     */
    val username: String?
    /**
     * Password used for authentication, used only if [username] is not `null`
     */
    val password: String?
}
