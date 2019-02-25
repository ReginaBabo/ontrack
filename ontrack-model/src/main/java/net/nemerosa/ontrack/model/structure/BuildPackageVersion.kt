package net.nemerosa.ontrack.model.structure

/**
 * [PackageVersion] associated to a [Build].
 *
 * @property packageVersion Version of a package
 * @property target Optional target of this package version
 */
class BuildPackageVersion(
        val packageVersion: PackageVersion,
        val target: Build?
)
