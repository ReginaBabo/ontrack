package net.nemerosa.ontrack.kdsl.core

import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * A resource in Ontrack, based on JSON data.
 */
abstract class Resource(ontrackConnector: OntrackConnector) : Connector(ontrackConnector)
