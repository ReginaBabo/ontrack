package net.nemerosa.ontrack.kdsl.model

/**
 * Searching a build on a project
 */
fun Project.searchBuilds(
        branchName: String? = null,
        buildName: String? = null,
        buildExactMatch: Boolean = false
): List<Build> {
    TODO("Project build search")
}