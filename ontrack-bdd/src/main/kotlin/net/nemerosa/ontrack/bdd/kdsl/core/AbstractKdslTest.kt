package net.nemerosa.ontrack.bdd.kdsl.core

import net.nemerosa.ontrack.kdsl.core.Ontrack

abstract class AbstractKdslTest {

    protected val ontrack: Ontrack = Ontrack.connect()

}