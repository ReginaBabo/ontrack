package net.nemerosa.ontrack.model.structure

/**
 * Management of [BuildPackageVersion]s.
 */
interface BuildPackageVersionService {

    /**
     * Adds or updates an entry (based on parent build and version)
     */
    fun saveBuildPackage(parent: Build, buildPackageVersion: BuildPackageVersion)

    /**
     * Gets packages for a parent build
     */
    fun getBuildPackages(parent: Build): List<BuildPackageVersion>

    /**
     * Loops on all unassigned packages.
     */
    fun onUnassignedPackages(project: Project, code: (Build, BuildPackageVersion) -> Unit)

    /**
     * Clears all packages for the build
     */
    fun clearBuildPackages(parent: Build)

}