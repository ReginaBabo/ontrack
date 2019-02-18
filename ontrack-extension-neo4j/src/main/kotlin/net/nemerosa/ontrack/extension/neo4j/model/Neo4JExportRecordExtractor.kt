package net.nemerosa.ontrack.extension.neo4j.model

interface Neo4JExportRecordExtractor<T> {

    val recordDefinitions: List<Neo4JExportRecordDef<T>>

    fun export(exporter: (record: T) -> Unit)

}
