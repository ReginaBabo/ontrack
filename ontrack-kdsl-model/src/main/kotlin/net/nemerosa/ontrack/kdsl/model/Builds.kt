package net.nemerosa.ontrack.kdsl.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import net.nemerosa.ontrack.kdsl.core.Ontrack

/**
 * Build entity
 *
 * @property name Name of this build
 * @property description Description of this build
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Build(
        id: Int,
        @JsonProperty
        override val projectId: Int,
        creation: Signature,
        val name: String,
        val description: String
) : ProjectEntityResource(id, creation) {

    override val entityType: String = "BUILD"

}

/**
 * Gets a build using its ID
 */
fun Ontrack.getBuildByID(id: Int): Build =
        ontrackConnector.get("structure/builds/$id")
                ?.adaptSignature()
                ?.toConnector()
                ?: throw EntityNotFoundException("build", id)

/**
 * Creates or gets a build and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the build
 * @param description Description of the build
 * @param initFn Code to run against the created build
 * @return Object return by [initFn]
 */
fun <T> Branch.build(
        name: String,
        description: String = "",
        initFn: Build.() -> T
): T {
    val build: Build? = project.searchBuilds(
            branchName = this.name,
            buildName = name,
            buildExactMatch = true
    ).firstOrNull()
    val actualBuild = if (build != null) {
        ontrackConnector.put(
                "structure/builds/${build.id}/update",
                mapOf(
                        "name" to name,
                        "description" to description
                )
        )
        ontrack.getBuildByID(build.id)
    } else {
        ontrackConnector.post(
                "structure/branches/${id}/builds/create",
                mapOf(
                        "name" to name,
                        "description" to description
                )
        ).adaptProjectId("branch.project").adaptSignature().toConnector()
    }
    return actualBuild.initFn()
}

/**
 * Creates or gets a build
 *
 * @param name Name of the build
 * @param description Description of the build
 * @return Creates build
 */
fun Branch.build(
        name: String,
        description: String = ""
) = build(name, description) { this }

/**
 * Previous build
 */
val Build.previousBuild: Build?
    get() = ontrackConnector.get("structure/builds/$id/previous")
            ?.adaptSignature()?.toConnector()

/**
 * Next build
 */
val Build.nextBuild: Build?
    get() = ontrackConnector.get("structure/builds/$id/next")
            ?.adaptSignature()?.toConnector()
