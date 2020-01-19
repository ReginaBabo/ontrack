package net.nemerosa.ontrack.kdsl.runner

import net.nemerosa.ontrack.kdsl.client.OntrackConnectorProperties
import org.kohsuke.args4j.Option

class KDSLRunnerOptions : OntrackConnectorProperties {

    @Option(name = "--url", aliases = ["-r"], usage = "Ontrack URL")
    override var uri: String = "http://localhost:8080"

    @Option(name = "--user", aliases = ["-u"], usage = "Ontrack user")
    override var username: String = "admin"

    @Option(name = "--password", aliases = ["-p"], usage = "Ontrack password")
    override var password: String = "admin"

}