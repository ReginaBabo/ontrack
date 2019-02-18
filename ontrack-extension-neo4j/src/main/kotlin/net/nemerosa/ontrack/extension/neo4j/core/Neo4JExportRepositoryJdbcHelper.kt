package net.nemerosa.ontrack.extension.neo4j.core

import net.nemerosa.ontrack.repository.support.AbstractJdbcRepository
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class Neo4JExportRepositoryJdbcHelper(dataSource: DataSource) : AbstractJdbcRepository(dataSource), Neo4JExportRepositoryHelper {

    override fun buildLinks(exporter: (Neo4JBuildLink) -> Unit) {
        namedParameterJdbcTemplate.jdbcOperations.query(
                "SELECT * FROM BUILD_LINKS ORDER BY ID DESC"
        ) { rs ->
            val from = rs.getInt("BUILDID")
            val to = rs.getInt("TARGETBUILDID")
            exporter(Neo4JBuildLink(from, to))
        }
    }

}