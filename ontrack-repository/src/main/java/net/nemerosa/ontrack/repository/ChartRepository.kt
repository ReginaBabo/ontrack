package net.nemerosa.ontrack.repository

import net.nemerosa.ontrack.model.structure.Branch
import java.time.LocalDateTime

interface ChartRepository {

    fun collectBuildRecords(branch: Branch, recording: (BuildChartRecord) -> Unit)

    fun collectValidationRunRecords(branch: Branch, recording: (ValidationRunChartRecord) -> Unit)

}

class BuildChartRecord(
        val name: String,
        val creation: LocalDateTime?,
        val time: Int?
)

class ValidationRunChartRecord(
        val build: String,
        val buildTimestamp: LocalDateTime,
        val runName: String,
        val runTime: Int?,
        val runCreation: LocalDateTime?,
        val runStatus: String
)
