package net.nemerosa.ontrack.extension.neo4j.core

interface Neo4JExportRepositoryHelper {

    fun buildLinks(exporter: (Neo4JBuildLink) -> Unit)

    fun promotions(exporter: (Neo4JPromotion) -> Unit)

}