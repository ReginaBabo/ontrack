package net.nemerosa.ontrack.graphql.schema

import graphql.Scalars.GraphQLBoolean
import graphql.schema.GraphQLFieldDefinition
import net.nemerosa.ontrack.graphql.support.GraphqlUtils
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
        private val packageVersion: GQLTypePackageVersion
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
                                it.name("linkedOnly")
                                        .description("Keeps only the versions which are linked to other builds in Ontrack")
                                        .type(GraphQLBoolean)
                            }
                            .type(packageVersion.typeRef)
                            .dataFetcher(GraphqlUtils.fetcher(
                                    Build::class.java
                            ) { build -> buildPackageVersionService.getBuildPackages(build) })
                            .build()
            )
        } else {
            emptyList()
        }
    }
}