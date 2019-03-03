package net.nemerosa.ontrack.graphql.schema

import graphql.Scalars.GraphQLBoolean
import graphql.schema.GraphQLFieldDefinition
import net.nemerosa.ontrack.graphql.support.GraphqlUtils.stdList
import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.BuildPackageVersionService
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.ProjectEntityType
import org.springframework.stereotype.Component

/**
 * Contributes the list of package versions to the Build type.
 */
@Component
class GQLBuildPackageVersionsFieldContributor(
        private val buildPackageVersionService: BuildPackageVersionService,
        private val buildPackageVersion: GQLTypeBuildPackageVersion
) : GQLProjectEntityFieldContributor {

    override fun getFields(
            projectEntityClass: Class<out ProjectEntity>,
            projectEntityType: ProjectEntityType
    ): List<GraphQLFieldDefinition> {
        return if (projectEntityType == ProjectEntityType.BUILD) {
            listOf(
                    GraphQLFieldDefinition.newFieldDefinition()
                            .name("packageVersions")
                            .description("List of package versions associated with this build.")
                            .argument {
                                it.name(ARG_LINKED_ONLY)
                                        .description("Keeps only the versions which are linked to other builds in Ontrack")
                                        .type(GraphQLBoolean)
                            }
                            .type(stdList(buildPackageVersion.typeRef))
                            .dataFetcher { environment ->
                                val build = environment.getSource<Build>()
                                val linkedOnly = environment.getArgument<Boolean>(ARG_LINKED_ONLY) ?: false
                                val versions = buildPackageVersionService.getBuildPackages(build)
                                if (linkedOnly) {
                                    versions.filter { it ->
                                        it.target != null
                                    }
                                } else {
                                    versions
                                }
                            }
                            .build()
            )
        } else {
            emptyList()
        }
    }

    companion object {
        private const val ARG_LINKED_ONLY = "linkedOnly"
    }
}