package net.nemerosa.ontrack.kdsl.test.app

import org.junit.runner.notification.RunNotifier
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SpringTestClassRunner(
        testClass: Class<*>,
        private val config: SpringTestConfig
) : BlockJUnit4ClassRunner(testClass) {

    private val logger: Logger = LoggerFactory.getLogger(SpringTestClassRunner::class.java)

    override fun run(notifier: RunNotifier) {
        logger.info("\n\n[TESTSUITE] $description\n")
        super.run(notifier)
    }

    override fun runChild(method: FrameworkMethod, notifier: RunNotifier) {
        val root = method.declaringClass.getAnnotation(SpringTest::class.java)
        val item = method.getAnnotation(SpringTest::class.java)
        val description = describeChild(method)
        if (!config.acceptTest(root, item)) {
            notifier.fireTestIgnored(description)
            return
        }
        // Default
        logger.info("\n\n[TEST] $description\n")
        // Context
        SpringTestContext.instance.set(
                SpringTestContext(description.displayName)
        )
        // Running
        super.runChild(method, notifier)
    }
}
