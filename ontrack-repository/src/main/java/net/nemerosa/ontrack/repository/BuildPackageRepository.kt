package net.nemerosa.ontrack.repository

import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.BuildPackageVersion

/**
 * Management of [BuildPackageVersion]s.
 */
interface BuildPackageRepository {

    /**
     * Adds or updates an entry (based on parent build and version)
     */
    fun saveBuildPackage(record: TBuildPackageVersion)

    /**
     * Gets packages for a parent build
     */
    fun getBuildPackages(parent: Build): List<TBuildPackageVersion>

    /**
     * Gets all unassigned packages.
     */
    fun getUnassignedPackages(): List<TBuildPackageVersion>

}