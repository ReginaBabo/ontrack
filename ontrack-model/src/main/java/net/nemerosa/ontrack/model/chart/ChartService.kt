package net.nemerosa.ontrack.model.chart

import net.nemerosa.ontrack.model.structure.Branch

interface ChartService {

    fun getBuildChart(branch: Branch): Chart

}