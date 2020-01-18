package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.kdsl.core.OntrackRoot

abstract class AbstractOntrackDSL : AbstractSteps() {

    protected val ontrack
        get() = OntrackRoot.connect(bddProperties.ontrack)

}