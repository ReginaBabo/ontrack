package net.nemerosa.ontrack.kdsl.runner

import net.nemerosa.ontrack.kdsl.model.Ontrack

interface KDSLRunnerFactory {

    fun createKDSLRunner(ontrack: Ontrack): KDSLRunner

}