package net.nemerosa.ontrack.bdd.model.steps

import net.nemerosa.ontrack.bdd.model.BDDProperties
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractSteps {

    @Autowired
    protected lateinit var bddProperties: BDDProperties

}