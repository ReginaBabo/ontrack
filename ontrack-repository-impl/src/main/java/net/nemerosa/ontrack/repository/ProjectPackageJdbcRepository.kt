package net.nemerosa.ontrack.repository

import net.nemerosa.ontrack.repository.support.AbstractJdbcRepository
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class ProjectPackageJdbcRepository(dataSource: DataSource) : AbstractJdbcRepository(dataSource), ProjectPackageRepository {

    override fun findProjectsWithPackageIdentifier(type: String, id: String): List<Int> {
        return namedParameterJdbcTemplate.queryForList(
                """
                    SELECT PROJECT
                    FROM PROJECT_PACKAGE
                    WHERE PACKAGE_TYPE = :type
                    AND PACKAGE_ID = :id
                """,
                params("type", type).addValue("id", id),
                Int::class.java
        )
    }

    override fun getPackagesForProject(project: Int): List<TProjectPackage> {
        return namedParameterJdbcTemplate.query(
                """
                    SELECT PACKAGE_TYPE, PACKAGE_ID
                    FROM PROJECT_PACKAGE
                    WHERE PROJECT = :project
                    ORDER BY PACKAGE_TYPE, PACKAGE_ID
                """,
                params("project", project)
        ) { rs, _ ->
            TProjectPackage(
                    type = rs.getString("PACKAGE_TYPE"),
                    id = rs.getString("PACKAGE_ID")
            )
        }
    }

    override fun setPackagesForProject(project: Int, packages: List<TProjectPackage>) {
        val params = params("project", project)
        // Deletes previous package information
        namedParameterJdbcTemplate.update(
                "DELETE FROM PROJECT_PACKAGE WHERE PROJECT = :project",
                params
        )
        // Adds all package ids
        packages.forEach { p ->
            namedParameterJdbcTemplate.update(
                    """
                        INSERT INTO PROJECT_PACKAGE(PROJECT, PACKAGE_TYPE, PACKAGE_ID)
                        VALUES (:project, :type, :id)
                    """,
                    params.addValue("type", p.type)
                            .addValue("id", p.id)
            )
        }
    }
}