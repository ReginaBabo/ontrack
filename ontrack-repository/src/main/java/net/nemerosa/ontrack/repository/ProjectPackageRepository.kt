package net.nemerosa.ontrack.repository

interface ProjectPackageRepository {
    /**
     * Finds a list of project using a package identifier.
     *
     * @param type Package type
     * @param id Package ID
     * @return List of project IDs having this package type
     */
    fun findProjectsWithPackageIdentifier(type: String, id: String): List<Int>

    /**
     * Gets the package IDs for a project
     *
     * @param project ID of the project
     * @return List of packages
     */
    fun getPackagesForProject(project: Int): List<TProjectPackage>

    /**
     * Sets the package IDs for a project
     *
     * @param project ID of the project
     * @param packages List of packages
     */
    fun setPackagesForProject(project: Int, packages: List<TProjectPackage>)

}