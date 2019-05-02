package net.nemerosa.ontrack.service.chart

import net.nemerosa.ontrack.model.chart.*
import net.nemerosa.ontrack.model.structure.Branch
import net.nemerosa.ontrack.repository.BuildChartRecord
import net.nemerosa.ontrack.repository.ChartRepository
import net.nemerosa.ontrack.repository.ValidationRunChartRecord
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChartServiceImpl(
        private val chartRepository: ChartRepository
) : ChartService {

    override fun getBuildChart(branch: Branch): Chart {
        return Chart(listOf(buildChartSeries(branch)) + runChartSeries(branch))
    }

    private fun runChartSeries(branch: Branch): List<ChartSeries> {
        // Index of points per validation stamp
        val index = mutableMapOf<String, MutableList<ChartPoint>>()
        // Collection of points
        chartRepository.collectValidationRunRecords(branch) { record: ValidationRunChartRecord ->
            val runName = record.runName
            val points = index.getOrPut(runName) { mutableListOf() }
            points += ChartPoint(
                    ref = ChartRef(
                            name = record.build,
                            timestamp = record.buildTimestamp
                    ),
                    status = record.runStatus,
                    duration = record.runTime,
                    // TODO Collects also the run data
                    data = null
            )
        }
        // Transforming into series
        return index.map { (name, points) ->
            ChartSeries(
                    type = "run",
                    name = name,
                    points = points
            )
        }
    }

    private fun buildChartSeries(branch: Branch): ChartSeries {
        val buildPoints = mutableListOf<ChartPoint>()
        chartRepository.collectBuildRecords(branch) { record: BuildChartRecord ->
            record.creation?.let { creation ->
                buildPoints.add(
                        ChartPoint(
                                ChartRef(
                                        name = record.name,
                                        timestamp = creation
                                ),
                                status = null,
                                duration = record.time,
                                data = null
                        )
                )
            }
        }
        val buildSeries = ChartSeries(
                type = "build",
                name = null,
                points = buildPoints
        )
        return buildSeries
    }

}