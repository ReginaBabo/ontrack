package net.nemerosa.ontrack.bdd

import net.nemerosa.ontrack.bdd.engine.BDDRunner

class BDDApp {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            BDDRunner(BDDTests::class).run(args)
        }
    }

}