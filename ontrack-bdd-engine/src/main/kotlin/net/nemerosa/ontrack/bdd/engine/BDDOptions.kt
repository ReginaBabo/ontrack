package net.nemerosa.ontrack.bdd.engine

import org.kohsuke.args4j.Option
import java.io.File

class BDDOptions {
    @Option(
            name = "--output",
            aliases = ["-o"],
            usage = "Report directory",
            metaVar = "DIR",
            required = false
    )
    var outputDir = File("build")

    @Option(
            name = "--output-file",
            aliases = ["-f"],
            usage = "XML JUnit report file name",
            metaVar = "NAME",
            required = false
    )
    var outputFileName = "bdd.xml"

    @Option(
            name = "--cucumber-options",
            aliases = ["--cucumber", "-c"],
            usage = "Cucumber options",
            metaVar = "OPTIONS",
            required = false
    )
    var cucumberOptions: String = ""

    fun log(logger: (String) -> Unit) {
        logger("[BDD] Output directory: $outputDir")
        logger("[BDD] Output file name: $outputFileName")
        logger("[BDD] Cucumber options: $cucumberOptions")
    }
}
