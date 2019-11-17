package net.nemerosa.ontrack.kdsl.test

import net.nemerosa.ontrack.kdsl.core.Ontrack

abstract class AbstractKdslTest {

    protected val ontrack: Ontrack = Ontrack.connect()

}