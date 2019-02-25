package net.nemerosa.ontrack.model.structure

/**
 * Association between a parent build and a list of packages.
 */
class BuildPackageVersionLink(
        val parent: Build,
        val packages: List<BuildPackageVersion>
)