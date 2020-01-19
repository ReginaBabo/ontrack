package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.kdsl.model.KDSLOntrack

abstract class AbstractOntrackDSL : AbstractSteps() {

    protected val ontrack
        get() = KDSLOntrack.connect(bddProperties.ontrack)

}