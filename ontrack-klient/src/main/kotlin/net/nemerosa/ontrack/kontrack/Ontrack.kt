package net.nemerosa.ontrack.kontrack

import net.nemerosa.ontrack.kontrack.support.parse

/**
 * Main client for accessing Ontrack.
 *
 * @property client Underlying client
 */
class Ontrack(
        val client: OntrackClient
) {

    /**
     * Gets the list of projects authorised for the user.
     */
    val projects: List<Project>
        get() = client.get("structure/projects")["resources"].map {
            it.parse<Project>()
        }

}