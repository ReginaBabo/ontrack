package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.kdsl.core.Ontrack

abstract class AbstractOntrackDSL : AbstractSteps() {

    protected val ontrack
        get() = Ontrack.connect(bddProperties.ontrack)

}