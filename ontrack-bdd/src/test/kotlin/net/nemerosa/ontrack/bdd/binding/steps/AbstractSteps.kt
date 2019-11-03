package net.nemerosa.ontrack.bdd.binding.steps

import net.nemerosa.ontrack.bdd.BDDProperties
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractSteps {

    @Autowired
    protected lateinit var bddProperties: BDDProperties

}