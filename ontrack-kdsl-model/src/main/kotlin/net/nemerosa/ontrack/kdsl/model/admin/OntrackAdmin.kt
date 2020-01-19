package net.nemerosa.ontrack.kdsl.model.admin

interface OntrackAdmin {

    /**
     * Access to the settings DSL
     */
    val settings: Settings

    /**
     * Account management
     */
    val accounts: AccountManagement

}