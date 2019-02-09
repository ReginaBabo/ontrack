package net.nemerosa.ontrack.model.structure

interface ProjectPackageService {

    /**
     * Finds a list of project using a package identifier.
     */
    fun findProjectsWithPackageIdentifier(id: PackageId): List<Project>

    /**
     * Gets the package IDs for a project
     *
     * @param project Project
     * @return List of packages
     */
    fun getPackagesForProject(project: Project): List<PackageId>

    /**
     * Sets the package IDs for a project
     *
     * @param project Project
     * @param packages List of packages
     */
    fun setPackagesForProject(project: Project, packages: List<PackageId>)

}