package net.nemerosa.ontrack.test.engine.junit

import org.junit.runner.JUnitCore
import org.junit.runner.Runner
import java.io.File

class JUnitRunner {

    /**
     * @param runner JUnit runner to launch
     * @param outputDir Output directory
     * @param outputFilename Name of the output
     */
    fun run(
            runner: Runner,
            outputDir: File,
            outputFilename: String
    ): Boolean = run(listOf(runner), outputDir, outputFilename)

    /**
     * @param runners JUnit runners to launch
     * @param outputDir Output directory
     * @param outputFilename Name of the output
     */
    fun run(
            runners: Collection<Runner>,
            outputDir: File,
            outputFilename: String
    ): Boolean {
        // JUnit runtime
        val junit = JUnitCore()

        // XML listener
        val xmlRunListener = XMLRunListener()
        junit.addListener(xmlRunListener)

        // Runs the tests
        val ok = runners.map {
            junit.run(it)
        }.all {
            it.wasSuccessful()
        }

        // Rendering
        xmlRunListener.render(File(outputDir, outputFilename))

        // Result
        return ok

    }
}
