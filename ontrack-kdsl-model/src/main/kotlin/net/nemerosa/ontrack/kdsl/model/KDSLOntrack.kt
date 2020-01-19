package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.dsl.*
import net.nemerosa.ontrack.dsl.admin.OntrackAdmin
import net.nemerosa.ontrack.kdsl.client.OntrackConnector
import net.nemerosa.ontrack.kdsl.client.OntrackConnectorProperties
import net.nemerosa.ontrack.kdsl.client.support.OntrackConnectorBuilder
import net.nemerosa.ontrack.kdsl.core.GraphQLParamImpl
import net.nemerosa.ontrack.kdsl.core.OntrackRoot
import net.nemerosa.ontrack.kdsl.core.type
import net.nemerosa.ontrack.kdsl.core.value
import net.nemerosa.ontrack.kdsl.model.admin.KDSLOntrackAdmin

class KDSLOntrack(ontrackConnector: OntrackConnector) : OntrackRoot(ontrackConnector), Ontrack {

    override fun asAnonymous(): Ontrack = KDSLOntrack(
            ontrackConnector.asAnonymous()
    )

    override val admin: OntrackAdmin = KDSLOntrackAdmin(ontrackConnector)

    override val settings: Settings = KDSLSettings(ontrackConnector)

    override val labels: Labels = KDSLLabels(ontrackConnector)

    override val projects: List<Project>
        get() = getResources("structure/projects") {
            KDSLProject(it, ontrackConnector)
        }

    override fun getProjects(name: String?, favoritesOnly: Boolean, propertyType: String?, propertyValue: String?): List<Project> {
        val decl: String
        val params = mutableListOf<GraphQLParamImpl>()
        when {
            name != null -> {
                decl = "(name: ${'$'}name)"
                params += "name" type "String!" value name
            }
            propertyType != null -> {
                decl = """withProperty: {type: ${'$'}propertyType, value: ${'$'}propertyValue}"""
                params += "propertyType" type "String" value propertyType
                params += "propertyValue" type "String" value propertyValue
            }
            favoritesOnly -> {
                decl = "(favourites: true)"
            }
            else -> {
                decl = ""
            }
        }
        val query = """
            projects$decl {
                json
            }
        """
        // Query
        return graphQLQuery(
                queryName = "ProjectList",
                query = query,
                params = params
        ).data["projects"].map { KDSLProject(it["json"], ontrackConnector) }
    }

    override fun getProjectByID(id: Int): Project =
            ontrackConnector.get("structure/projects/$id")?.let {
                KDSLProject(it, ontrackConnector)
            } ?: throw EntityNotFoundException("project", id)

    override fun findProjectByName(name: String): Project? =
            getProjects(name).firstOrNull()

    override fun createProject(
            name: String,
            description: String,
            disabled: Boolean
    ): Project = postAndConvert(
                    "structure/projects/create",
                    mapOf(
                            "name" to name,
                            "description" to description,
                            "disabled" to disabled
                    )
            ) { KDSLProject(it, ontrackConnector) }

    override fun findBranchByName(project: String, branch: String): Branch? =
            ontrackConnector.get("structure/entity/branch/$project/$branch")
                    ?.let { KDSLBranch(it, ontrackConnector) }

    override fun findBuildByName(project: String, branch: String, build: String): Build? =
            ontrackConnector.get("structure/entity/build/$project/$branch/$build")
                    ?.let { KDSLBuild(it, ontrackConnector) }

    override fun getBuildByID(id: Int): Build =
            ontrackConnector.get("structure/builds/$id")
                    ?.let { KDSLBuild(it, ontrackConnector) }
                    ?: throw EntityNotFoundException("build", id)

    override fun getPromotionLevelByID(id: Int): PromotionLevel =
            ontrackConnector.get("structure/promotionLevels/$id")
                    ?.let { KDSLPromotionLevel(it, ontrackConnector) }
                    ?: throw EntityNotFoundException("promotion level", id)

    companion object {

        /**
         * Utility method to get a [KDSLOntrack] instance.
         */
        @JvmStatic
        fun connect(properties: OntrackConnectorProperties? = null): Ontrack {
            val ontrackConnector = OntrackConnectorBuilder.getOrCreateFromEnv(properties)
            return KDSLOntrack(ontrackConnector)
        }
    }
}