package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.kdsl.impl.KDSLOntrack

abstract class AbstractOntrackDSL : AbstractSteps() {

    protected val ontrack
        get() = KDSLOntrack.connect(bddProperties.ontrack)

}