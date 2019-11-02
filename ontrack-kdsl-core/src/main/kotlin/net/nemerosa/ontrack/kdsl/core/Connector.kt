package net.nemerosa.ontrack.kdsl.core

import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * Object which gets an internal link to Ontrack through an [OntrackConnector].
 */
abstract class Connector(
        ontrackConnector: OntrackConnector
) : OntrackConnector by ontrackConnector
