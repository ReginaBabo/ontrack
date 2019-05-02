package net.nemerosa.ontrack.repository

import net.nemerosa.ontrack.model.structure.Branch
import net.nemerosa.ontrack.repository.support.AbstractJdbcRepository
import net.nemerosa.ontrack.repository.support.getNullableInt
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class ChartJdbcRepository(dataSource: DataSource) : AbstractJdbcRepository(dataSource), ChartRepository {

    override fun collectBuildRecords(branch: Branch, recording: (BuildChartRecord) -> Unit) {
        val sql = """
            select b.name as name, b.creation as timestamp, bi.run_time as duration
            from builds b
            left join run_info bi on bi.build = b.id
            where b.branchid = :branchId
            order by b.id desc
            """
        namedParameterJdbcTemplate.query(sql, params("branchId", branch.id())) { rs ->
            recording(
                    BuildChartRecord(
                            name = rs.getString("name"),
                            creation = dateTimeFromDB(rs.getString("timestamp")),
                            time = rs.getNullableInt("duration")
                    )
            )
        }
    }

    override fun collectValidationRunRecords(branch: Branch, recording: (ValidationRunChartRecord) -> Unit) {
        val sql = """
            select b.name as name, b.creation as timestamp, vs.name as vs_name, ri.run_time as vs_time, rs.creation as vs_timestamp, rs.validationrunstatusid as vs_status
            from builds b
            left join validation_runs r on r.buildid = b.id
            left join validation_stamps vs on vs.id = r.validationstampid
            left join run_info ri on ri.validation_run = r.id
            left join validation_run_statuses rs on rs.validationrunid = r.id
            where b.branchid = :branchId
            order by b.id desc
            """
        namedParameterJdbcTemplate.query(sql, params("branchId", branch.id())) { rs ->
            val runName = rs.getString("vs_name")
            runName?.let {
                recording(
                        ValidationRunChartRecord(
                                build = rs.getString("name"),
                                buildTimestamp = dateTimeFromDB(rs.getString("timestamp")),
                                runName = it,
                                runCreation = dateTimeFromDB(rs.getString("vs_timestamp")),
                                runTime = rs.getNullableInt("vs_time"),
                                runStatus = rs.getString("vs_status")
                        )
                )
            }
        }
    }
}