package net.nemerosa.ontrack.kdsl.test.app

import net.nemerosa.ontrack.test.engine.junit.JUnitRunner
import org.junit.runner.Runner
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class SpringTestRunner(
        private val applicationContext: ApplicationContext,
        private val springTestProperties: SpringTestProperties
) : TestRunner {

    private val logger: Logger = LoggerFactory.getLogger(SpringTestRunner::class.java)

    override fun run(): Boolean {
        // Gets the list of tests
        val allTests = applicationContext.getBeansWithAnnotation<SpringTest>()
        logger.info("Total number of tests: ${allTests.size}")
        // Filters the tests
        val tests = filter(allTests)
        logger.info("Number of filtered tests: ${tests.size}")
        // Creates all test runners
        val runners = tests.map { createRunner(it) }
        // Creates the overall runner
        val runner = JUnitRunner()
        // Runs all runners using the overall runner
        return runner.run(
                runners = runners,
                outputDir = springTestProperties.output.dir,
                outputFilename = springTestProperties.output.name
        ).apply {
            logger.info("Overall test OK: $this")
        }
    }

    private fun filter(allTests: Map<String, Any>): List<Class<*>> =
            allTests
                    .filter { (name, _) ->
                        val acceptanceTest = applicationContext.findAnnotationOnBean(name, SpringTest::class.java)
                        springTestProperties.config.acceptTest(acceptanceTest)
                    }
                    .map { it.value.javaClass }

    private fun createRunner(testClass: Class<*>): Runner =
            SpringTestClassRunner(testClass, springTestProperties.config)

}