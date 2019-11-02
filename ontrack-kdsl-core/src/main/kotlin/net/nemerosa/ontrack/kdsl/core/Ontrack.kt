package net.nemerosa.ontrack.kdsl.core

import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * Root object of the Ontrack model. Typically extended by the model and
 * extensions for the Kotlin DSL.
 */
class Ontrack(ontrackConnector: OntrackConnector) : Connector(ontrackConnector)
