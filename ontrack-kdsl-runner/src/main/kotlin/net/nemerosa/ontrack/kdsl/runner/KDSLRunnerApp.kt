package net.nemerosa.ontrack.kdsl.runner

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.nemerosa.ontrack.kdsl.impl.KDSLOntrack
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File

@SpringBootApplication
class KDSLRunnerApp(
        private val runnerFactory: KDSLRunnerFactory
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Parsing of options
        val options = parseOptions(args)
        if (options != null) {
            run(options)
        }
    }

    private fun parseOptions(args: Array<out String?>): KDSLRunnerOptions? {
        // Parsing of options
        val options = KDSLRunnerOptions()
        val parser = CmdLineParser(options)
        // Help?
        return if (args.contains("--help") || args.contains("-h")) {
            parser.printUsage(System.out)
            null
        } else {
            // Parsing
            try {
                parser.parseArgument(*args)
                options
            } catch (ex: CmdLineException) {
                parser.printUsage(System.out)
                throw ex
            }
        }
    }

    private fun run(options: KDSLRunnerOptions) {
        // Gets the connection
        val ontrack = KDSLOntrack.connect(options)
        // Gets the script to run
        val script = options.script
        // Gets the bound variables
        val bindings = options.bindings
        // Creates a runner
        val runner = runnerFactory.createKDSLRunner(ontrack)
        // Launches the runner
        val result = runner.kdsl(script, bindings)
        // Outputs the result
        options.write(result)
    }

    private val KDSLRunnerOptions.script: String
        get() =
            if (path == "-") {
                System.`in`.reader().readText()
            } else {
                File(path).reader().readText()
            }

    private val KDSLRunnerOptions.bindings: Map<String, Any?>
        get() {
            val result = mutableMapOf<String, Any?>()
            // Values provided on the command line
            result.putAll(values)
            // Values provided in a file
            valueFile?.apply {
                // Gets the file binder
                val fileBinder = valueFileType.fileBinder
                // Extracts the bindings from the file and adds them to the list
                result.putAll(fileBinder.readBindings(this))
            }
            // OK
            return result.toMap()
        }

    private fun KDSLRunnerOptions.write(result: JsonNode) {
        if (!discardResult) {
            val objectMapper = ObjectMapper()
            objectMapper.writeValue(
                    System.out,
                    result
            )
        }
    }

}

fun main(args: Array<String>) {
    runApplication<KDSLRunnerApp>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}