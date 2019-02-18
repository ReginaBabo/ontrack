package net.nemerosa.ontrack.extension.neo4j.model

data class SequenceNeo4JExportRecordExtractor<T>(
        val collectionSupplier: () -> Sequence<T>,
        override val recordDefinitions: List<Neo4JExportRecordDef<T>>
) : Neo4JExportRecordExtractor<T> {
    override fun export(exporter: (record: T) -> Unit) {
        // Gets the list of items
        val items = collectionSupplier()
        // Exports each items
        items.forEach { exporter(it) }
    }
}
