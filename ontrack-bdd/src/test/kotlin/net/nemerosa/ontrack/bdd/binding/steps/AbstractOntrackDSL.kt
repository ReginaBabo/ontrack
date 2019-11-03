package net.nemerosa.ontrack.bdd.binding.steps

import net.nemerosa.ontrack.kdsl.core.Ontrack

abstract class AbstractOntrackDSL : AbstractSteps() {

    protected val ontrack
        get() = Ontrack.connect(bddProperties.ontrack)

}