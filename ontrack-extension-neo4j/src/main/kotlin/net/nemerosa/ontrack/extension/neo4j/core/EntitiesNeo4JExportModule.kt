package net.nemerosa.ontrack.extension.neo4j.core

import net.nemerosa.ontrack.extension.api.ExtensionManager
import net.nemerosa.ontrack.extension.neo4j.Neo4JNodeContributor
import net.nemerosa.ontrack.extension.neo4j.model.Neo4JExportModule
import net.nemerosa.ontrack.extension.neo4j.model.extractors
import net.nemerosa.ontrack.extension.neo4j.model.rel
import net.nemerosa.ontrack.model.structure.*
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
class EntitiesNeo4JExportModule(
        private val structureService: StructureService,
        private val extensionManager: ExtensionManager,
        private val neo4JExportRepositoryHelper: Neo4JExportRepositoryHelper
) : Neo4JExportModule {

    override val recordExtractors
        get() = extractors {
            extractor<Project> {
                records { structureService.projectList.asSequence() }
                node("Project") {
                    id(entityId())
                    column("name" to Project::getName)
                    column("description" to Project::getDescription)
                    column("disabled" to Project::isDisabled)
                    // TODO creator
                    // TODO creation
                }
            }
            extractor<Branch> {
                records { structureService.projectList.asSequence().flatMap { structureService.getBranchesForProject(it.id).asSequence() } }
                node("Branch") {
                    id(entityId())
                    column("name" to Branch::getName)
                    column("description" to Branch::getDescription)
                    column("disabled" to Branch::isDisabled)
                    // TODO creator
                    // TODO creation
                }
                rel("BRANCH_OF") { b -> b.project }
            }
            extractor<Build> {
                recorder { exporter ->
                    structureService.projectList.asSequence()
                            .flatMap { structureService.getBranchesForProject(it.id).asSequence() }
                            .forEach { branch ->
                                structureService.findBuild(
                                        branch.id,
                                        Predicate { build ->
                                            exporter(build)
                                            false
                                        },
                                        BuildSortDirection.FROM_OLDEST
                                )
                            }
                }
                node("Build") {
                    id(entityId())
                    column("build" to Build::getName)
                    column("description" to Build::getDescription)
                    // TODO creator
                    // TODO creation
                    nodeContributors<Build>(extensionManager.getExtensions(Neo4JNodeContributor::class.java))
                }
                rel("BUILD_OF") { b -> b.branch }
            }
            extractor<Neo4JBuildLink> {
                recorder { exporter ->
                    neo4JExportRepositoryHelper.buildLinks(exporter)
                }
                rel("DEPENDS_ON") {
                    start { link -> entityId<Build>(link.from) }
                    end { link -> entityId<Build>(link.to) }
                }
            }
            // Promotions
            extractor<Neo4JPromotion> {
                recorder { exporter ->
                    neo4JExportRepositoryHelper.promotions(exporter)
                }
                node("Promotion") {
                    id { it.uuid }
                    column("name" to Neo4JPromotion::name)
                    column("description" to Neo4JPromotion::description)
                    // TODO creator
                    // TODO creation
                }
                rel("PROMOTION_OF") {
                    start { it.uuid }
                    end { entityId<Project>(it.project) }
                }
            }
            // Promoted builds
            extractor<Neo4JPromotedBuild> {
                recorder { exporter ->
                    neo4JExportRepositoryHelper.promotedBuilds(exporter)
                }
                rel("PROMOTED_TO") {
                    start { entityId<Build>(it.build) }
                    end { it.promotionUuid }
                    column("description" to Neo4JPromotedBuild::description)
                    // TODO creator
                    // TODO creation
                }
            }
        }
}