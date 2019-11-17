package net.nemerosa.ontrack.kdsl.test

import net.nemerosa.ontrack.kdsl.test.app.TestRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class KDSLApp(
        private val runners: List<TestRunner>
) : CommandLineRunner {
    override fun run(vararg args: String) {
        val ok = runners.all { it.run() }
        if (!ok) {
            exitProcess(1)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<KDSLApp>(*args)
}
