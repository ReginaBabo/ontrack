package net.nemerosa.ontrack.bdd.engine

import cucumber.runtime.RuntimeOptions
import net.nemerosa.ontrack.bdd.engine.support.junit.JUnitRunner
import net.serenitybdd.cucumber.CucumberWithSerenity
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.system.exitProcess

class BDDRunner(
        private val testClass: KClass<*>
) {
    private val logger = LoggerFactory.getLogger(BDDRunner::class.java)

    fun run(args: Array<String>) {

        // Parsing of command line arguments
        val options = BDDOptions()

        val parser = CmdLineParser(options)
        try {
            parser.parseArgument(*args)
        } catch (ex: CmdLineException) {
            // handling of wrong arguments
            System.err.println(ex.message)
            parser.printUsage(System.err)
            System.exit(1)
        }

        // Logging
        options.log(logger::info)

        val runner = JUnitRunner()

        val runtimeOptions = RuntimeOptions(options.cucumberOptions)

        CucumberWithSerenity.setRuntimeOptions(
                runtimeOptions
        )

        System.setProperty("acceptance.output", options.outputDir.absolutePath)

        val ok = runner.run(
                runner = CucumberWithSerenity(testClass.java),
                outputDir = options.outputDir,
                outputFilename = options.outputFileName
        )

        if (!ok) {
            exitProcess(1)
        }
    }

}