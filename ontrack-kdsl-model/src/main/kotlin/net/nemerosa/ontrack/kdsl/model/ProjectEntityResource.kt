package net.nemerosa.ontrack.kdsl.model

/**
 * Entity resource linked to a project
 *
 * @property creation Creation time and author
 */
abstract class ProjectEntityResource(
        id: Int,
        val creation: Signature
) : EntityResource(id)
