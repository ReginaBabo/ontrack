package net.nemerosa.ontrack.bdd

import net.nemerosa.ontrack.bdd.engine.BDDRunner
import kotlin.system.exitProcess

class BDDApp {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val ok = BDDRunner(BDDTests::class).run(args)
            if (!ok) {
                exitProcess(1)
            }
        }
    }

}