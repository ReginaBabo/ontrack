package net.nemerosa.ontrack.kontrack

import net.nemerosa.ontrack.client.ClientNotFoundException
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

    /**
     * Creating a project
     */
    fun newProject(name: String, description: String = ""): Project {
        val response = client.post(
                "structure/projects/create",
                mapOf(
                        "name" to name,
                        "description" to description
                )
        )
        return response.parse()
    }

    /**
     * Gets a project by name
     */
    fun findProjectByName(name: String): Project? {
        return try {
            val projectNode = client.get("structure/entity/project/$name")
            return projectNode.parse()
        } catch (_: ClientNotFoundException) {
            return null
        }
    }

}