package net.nemerosa.ontrack.model.structure

/**
 * Bulk upload of build links based on package versions.
 */
interface BuildPackageVersionUploadService {

    /**
     * Upload
     */
    fun uploadAndResolvePackageVersions(parent: Build, packages: List<PackageVersion>)
}