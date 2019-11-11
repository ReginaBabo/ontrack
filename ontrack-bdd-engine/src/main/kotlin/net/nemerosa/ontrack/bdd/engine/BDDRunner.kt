package net.nemerosa.ontrack.bdd.engine

import cucumber.runtime.RuntimeOptions
import net.nemerosa.ontrack.bdd.engine.support.junit.JUnitRunner
import net.serenitybdd.cucumber.CucumberWithSerenity
import net.thucydides.core.model.TestResult
import net.thucydides.core.reports.html.HtmlAggregateStoryReporter
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.reflect.KClass

class BDDRunner(
        private val testClass: KClass<*>
) {
    private val logger = LoggerFactory.getLogger(BDDRunner::class.java)

    fun run(args: Array<String>): Boolean {

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

        val ok = runner.run(
                runner = CucumberWithSerenity(testClass.java),
                outputDir = options.outputDir,
                outputFilename = options.outputFileName
        )

        // Generation of reports
        val sourceDirectory = File("target/site/serenity/")
        val reporter = HtmlAggregateStoryReporter("BDD")
        reporter.sourceDirectory = sourceDirectory
        reporter.outputDirectory = sourceDirectory
        val outcome = reporter.generateReportsForTestResultsFrom(sourceDirectory)

        return outcome.result.isAtLeast(TestResult.SUCCESS)
    }

}