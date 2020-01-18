package net.nemerosa.ontrack.kdsl.core

import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * Root object of the Ontrack model. Typically extended by the model and
 * extensions for the Kotlin DSL.
 */
abstract class OntrackRoot(ontrackConnector: OntrackConnector) : Connector(ontrackConnector) {

}
