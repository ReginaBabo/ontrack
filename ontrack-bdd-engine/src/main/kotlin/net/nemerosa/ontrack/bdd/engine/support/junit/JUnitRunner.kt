package net.nemerosa.ontrack.bdd.engine.support.junit

import org.junit.runner.JUnitCore
import org.junit.runner.Result
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
    ): Boolean {
        // JUnit runtime
        val junit = JUnitCore()

        // XML listener
        val xmlRunListener = XMLRunListener()
        junit.addListener(xmlRunListener)

        // Runs the tests
        val result: Result = junit.run(runner)

        // Rendering
        xmlRunListener.render(File(outputDir, outputFilename))

        // Result
        return result.wasSuccessful()

    }
}
