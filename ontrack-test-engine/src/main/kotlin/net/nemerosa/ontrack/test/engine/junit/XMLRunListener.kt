package net.nemerosa.ontrack.test.engine.junit

import net.nemerosa.ontrack.test.engine.xml.Xml
import org.apache.commons.lang3.exception.ExceptionUtils
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener
import java.io.File

class XMLRunListener : RunListener() {

    private val runs = mutableMapOf<Description, TestRun>()

    private class TestRun(
            val description: Description
    ) {

        private val start: Long = System.currentTimeMillis()
        private var end: Long? = null
        var error: Failure? = null
        var failure: Failure? = null
        var ignored = false

        val time: Long
            get() = if (ignored) 0 else end?.let { it - start } ?: 0

        fun end() {
            end = System.currentTimeMillis()
        }

    }

    override fun testStarted(description: Description) {
        runs[description] = TestRun(description)
    }

    override fun testFinished(description: Description) {
        runs[description]?.end()
    }

    override fun testFailure(failure: Failure) {
        runs[failure.description]?.error = failure
    }

    override fun testAssumptionFailure(failure: Failure) {
        runs[failure.description]?.failure = failure
    }

    override fun testIgnored(description: Description) {
        val run = TestRun(description)
        run.ignored = true
        runs[description] = run
    }

    fun render(file: File) {
        file.parentFile?.mkdirs()
        testsuite(
                tests = runs.size,
                skipped = runs.values.count { it.ignored },
                failures = runs.values.count { it.failure != null },
                errors = runs.values.count { it.error != null },
                time = ((runs.values.sumByDouble { it.time.toDouble() }) / 1000L)
        ) {
            runs.values.forEach { run ->
                testcase(
                        name = run.description.methodName,
                        classname = run.description.className,
                        time = (run.time.toDouble() / 1000)
                ) {
                    if (run.ignored) {
                        skipped()
                    }
                    run.error?.apply {
                        failure(
                                message = message,
                                type = this::class.java.name,
                                errorMessage = errorMessage()
                        )
                    }
                    run.failure?.apply {
                        failure(
                                message = message,
                                type = this::class.java.name,
                                errorMessage = errorMessage()
                        )
                    }
                }
            }
        } to file
    }

    private fun testsuite(
            tests: Int,
            skipped: Int,
            failures: Int,
            errors: Int,
            time: Double,
            code: TestSuite.() -> Unit
    ): Xml = Xml.document("testsuite") {
        "tests" to tests
        "skipped" to skipped
        "failures" to failures
        "errors" to errors
        "time" to time
        TestSuite(this).code()
    }

    private class TestSuite(private val e: Xml.XmlElement) {
        fun testcase(
                name: String,
                classname: String,
                time: Double,
                code: TestCase.() -> Unit
        ) {
            e.element("testcase") {
                "name" to name
                "classname" to classname
                "time" to time
                TestCase(this).code()
            }
        }
    }

    private class TestCase(private val e: Xml.XmlElement) {
        fun skipped() {
            e.element("skipped")
        }

        fun failure(message: String, type: String, errorMessage: String) {
            e.element("failure") {
                "message" to message
                "type" to type
                +errorMessage
            }
        }
    }

    private fun Failure.errorMessage() =
            ExceptionUtils.getStackTrace(this.exception)

}
