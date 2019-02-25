package net.nemerosa.ontrack.repository

import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.repository.support.AbstractJdbcRepository
import net.nemerosa.ontrack.repository.support.getNullableInt
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.sql.DataSource

@Repository
class BuildPackageJdbcRepository(dataSource: DataSource) : AbstractJdbcRepository(dataSource), BuildPackageRepository {

    override fun saveBuildPackage(record: TBuildPackageVersion) {
        namedParameterJdbcTemplate.update(
                """
                    INSERT INTO BUILD_PACKAGE_VERSIONS(BUILD, PACKAGE_TYPE, PACKAGE_ID, PACKAGE_VERSION, CREATION, TARGET)
                    VALUES (:build, :type, :id, :version, :creation, :target)
                    ON CONFLICT (BUILD, PACKAGE_TYPE, PACKAGE_ID, PACKAGE_VERSION) DO
                    UPDATE SET CREATION = :creation, TARGET = :target
                """,
                params("build", record.parent)
                        .addValue("type", record.type)
                        .addValue("id", record.id)
                        .addValue("version", record.version)
                        .addValue("creation", dateTimeForDB(record.creation))
                        .addValue("target", record.target)
        )
    }

    override fun getBuildPackages(parent: Build): List<TBuildPackageVersion> {
        return namedParameterJdbcTemplate.query(
                """
                    SELECT *
                    FROM BUILD_PACKAGE_VERSIONS
                    WHERE BUILD = :build
                    ORDER BY PACKAGE_TYPE, PACKAGE_ID, PACKAGE_VERSION
                """,
                params("build", parent.id())
        ) { rs, _ -> toBuildPackageVersion(rs) }
    }

    override fun getUnassignedPackages(): List<TBuildPackageVersion> {
        return namedParameterJdbcTemplate.query(
                """
                    SELECT *
                    FROM BUILD_PACKAGE_VERSIONS
                    ORDER BY BUILD, PACKAGE_TYPE, PACKAGE_ID, PACKAGE_VERSION
                """
        ) { rs, _ -> toBuildPackageVersion(rs) }
    }

    private fun toBuildPackageVersion(rs: ResultSet): TBuildPackageVersion {
        return TBuildPackageVersion(
                parent = rs.getInt("BUILD"),
                type = rs.getString("PACKAGE_TYPE"),
                id = rs.getString("PACKAGE_ID"),
                version = rs.getString("PACKAGE_VERSION"),
                creation = dateTimeFromDB(rs.getString("CREATION")),
                target = rs.getNullableInt("TARGET")
        )
    }
}