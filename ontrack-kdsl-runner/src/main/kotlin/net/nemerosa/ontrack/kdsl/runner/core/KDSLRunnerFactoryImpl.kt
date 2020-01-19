package net.nemerosa.ontrack.kdsl.runner.core

import net.nemerosa.ontrack.kdsl.model.Ontrack
import net.nemerosa.ontrack.kdsl.runner.KDSLRunner
import net.nemerosa.ontrack.kdsl.runner.KDSLRunnerFactory
import org.springframework.stereotype.Component

@Component
class KDSLRunnerFactoryImpl : KDSLRunnerFactory {

    override fun createKDSLRunner(ontrack: Ontrack): KDSLRunner = KDSLRunnerImpl(ontrack)

}