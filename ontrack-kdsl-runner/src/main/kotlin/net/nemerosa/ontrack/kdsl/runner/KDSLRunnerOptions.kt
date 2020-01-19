package net.nemerosa.ontrack.kdsl.runner

import net.nemerosa.ontrack.kdsl.client.OntrackConnectorProperties
import net.nemerosa.ontrack.kdsl.runner.binding.FileBinderType
import org.kohsuke.args4j.Option
import org.kohsuke.args4j.spi.MapOptionHandler
import java.io.File

class KDSLRunnerOptions : OntrackConnectorProperties {

    @Option(name = "--url", aliases = ["-r"], usage = "Ontrack URL")
    override var uri: String = "http://localhost:8080"

    @Option(name = "--user", aliases = ["-u"], usage = "Ontrack user")
    override var username: String = "admin"

    @Option(name = "--password", aliases = ["-p"], usage = "Ontrack password")
    override var password: String = "admin"

    @Option(name = "--file", aliases = ["-f"], usage = "Path to the script file or '-' if script is provided on the standard input (default)")
    var path: String = "-"

    @Option(name = "--value", aliases = ["-v"], usage = "Name and values to bind to the script, using name=value format", handler = MapOptionHandler::class)
    var values: Map<String, String> = mutableMapOf()

    @Option(name = "--values", aliases = ["-i"], usage = "Path to a file which contains name/value pairs to inject into the script")
    var valueFile: File? = null

    @Option(name = "--type", aliases = ["-t"], usage = "Type of value file")
    var valueFileType: FileBinderType = FileBinderType.properties

    @Option(name = "--discard-result", aliases = ["-d"], usage = "If set, the result of the script is not written out, only the list of variables set by the script if any")
    var discardResult: Boolean = false

}