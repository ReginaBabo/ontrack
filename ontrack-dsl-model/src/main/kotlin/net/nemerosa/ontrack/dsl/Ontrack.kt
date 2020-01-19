package net.nemerosa.ontrack.dsl

interface Ontrack {

    fun asAnonymous(): Ontrack

    /**
     * Access to the settings DSL
     */
    val settings: Settings

    /**
     * Gets the list of all projects
     */
    val projects: List<Project>

    /**
     * Getting (filtered) list of projects.
     *
     * @param name Filter on project name
     * @param favoritesOnly `true` if only the favorite projects must be returned
     * @param propertyType Filter on property type assigned to project
     * @param propertyValue Filter on [property type][propertyType] and property value assigned to project
     */
    fun getProjects(
            name: String? = null,
            favoritesOnly: Boolean = false,
            propertyType: String? = null,
            propertyValue: String? = null
    ): List<Project>

    /**
     * Gets a project by its ID.
     *
     * @param id ID of the project
     * @return Project
     */
    fun getProjectByID(id: Int): Project

    /**

     * Gets a project by its name.
     *
     * @param name Exact name of the project
     * @return Project for this name, or `null` if not found.
     */
    fun findProjectByName(name: String): Project?

    /**
     * Creates a project.
     *
     * @param name Name of the project
     * @param description Description of the project
     * @param disabled State of the project
     * @return Created project
     */
    fun createProject(
            name: String,
            description: String = "",
            disabled: Boolean = false
    ): Project

    /**
     * Looks for a build by name. Fails if not found.
     */
    fun build(project: String, branch: String, build: String): Build

    /**
     * Gets a build using its ID
     */
    fun getBuildByID(id: Int): Build

}