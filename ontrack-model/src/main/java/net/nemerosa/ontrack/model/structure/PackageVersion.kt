package net.nemerosa.ontrack.model.structure

/**
 * Association of a [PackageId] and a a given version.
 */
class PackageVersion(
        val packageId: PackageId,
        val version: String
)
