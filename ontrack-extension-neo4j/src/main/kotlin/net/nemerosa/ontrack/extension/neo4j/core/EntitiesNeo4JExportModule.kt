package net.nemerosa.ontrack.extension.neo4j.core

import net.nemerosa.ontrack.extension.neo4j.model.Neo4JExportModule
import net.nemerosa.ontrack.extension.neo4j.model.extractors
import net.nemerosa.ontrack.extension.neo4j.model.rel
import net.nemerosa.ontrack.model.structure.*
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
class EntitiesNeo4JExportModule(
        private val structureService: StructureService
) : Neo4JExportModule {
    override val recordExtractors = extractors {
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
                column("name" to Build::getName)
                column("description" to Build::getDescription)
                // TODO creator
                // TODO creation
            }
            rel("BUILD_OF") { b -> b.branch }
        }
    }
}