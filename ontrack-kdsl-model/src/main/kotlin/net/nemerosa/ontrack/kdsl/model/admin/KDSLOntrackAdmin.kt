package net.nemerosa.ontrack.kdsl.model.admin

import net.nemerosa.ontrack.dsl.admin.AccountManagement
import net.nemerosa.ontrack.dsl.admin.OntrackAdmin
import net.nemerosa.ontrack.dsl.admin.Settings
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.core.Connector

class KDSLOntrackAdmin(ontrackConnector: OntrackConnector) : Connector(ontrackConnector), OntrackAdmin {

    override val settings: Settings = KDSLSettings(ontrackConnector)

    override val accounts: AccountManagement = KDSLAccountManagement(ontrackConnector)
}
