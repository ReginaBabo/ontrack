package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.kdsl.client.OntrackConnector

/**
 * Entity resource linked to a project
 *
 * @property creation Creation time and author
 */
abstract class ProjectEntityResource(
        ontrackConnector: OntrackConnector,
        id: Int,
        val creation: Signature
) : EntityResource(ontrackConnector, id)
