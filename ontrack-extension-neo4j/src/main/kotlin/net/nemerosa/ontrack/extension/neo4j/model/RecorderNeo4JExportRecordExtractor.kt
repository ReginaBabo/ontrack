package net.nemerosa.ontrack.extension.neo4j.model

data class RecorderNeo4JExportRecordExtractor<T>(
        val recorder: ((T) -> Unit) -> Unit,
        override val recordDefinitions: List<Neo4JExportRecordDef<T>>
) : Neo4JExportRecordExtractor<T> {
    override fun export(exporter: (record: T) -> Unit) {
        recorder(exporter)
    }
}
