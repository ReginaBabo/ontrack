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

    override fun promotions(exporter: (Neo4JPromotion) -> Unit) {
        namedParameterJdbcTemplate.jdbcOperations.query(
                """
                    SELECT distinct on (b.PROJECTID, pl.NAME)
                    b.PROJECTID, pl.NAME, pl.DESCRIPTION
                    FROM   PROMOTION_LEVELS pl
                    INNER JOIN BRANCHES b ON pl.BRANCHID = b.ID
                    ORDER  BY b.PROJECTID, pl.NAME
                """
        ) { rs ->
            exporter(
                    Neo4JPromotion(
                            project = rs.getInt("PROJECTID"),
                            name = rs.getString("NAME"),
                            description = rs.getString("DESCRIPTION")
                    )
            )
        }
    }

    override fun promotedBuilds(exporter: (Neo4JPromotedBuild) -> Unit) {
        namedParameterJdbcTemplate.jdbcOperations.query(
                """
                    SELECT r.BUILDID, r.CREATION, r.CREATOR, r.DESCRIPTION, p.NAME, b.PROJECTID
                    FROM PROMOTION_RUNS r
                    INNER JOIN PROMOTION_LEVELS p ON p.ID = r.PROMOTIONLEVELID
                    INNER JOIN BRANCHES b ON b.ID = p.BRANCHID
                """
        ) { rs ->
            exporter(
                    Neo4JPromotedBuild(
                            build = rs.getInt("BUILDID"),
                            description = rs.getString("DESCRIPTION") ?: "",
                            promotion = rs.getString("NAME"),
                            project = rs.getInt("PROJECTID")
                    )
            )
        }
    }
}