package net.nemerosa.ontrack.boot.support

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File

@Component
@ConfigurationProperties(prefix = "ontrack.dev")
class DevSettings {

    var web = defaultWeb
    var dev = "build/web/dev"
    var prod = "build/web/prod"
    var src = "src"
    var vendor = "vendor"

    val rootDir: File
        get() {
            val currentDir = File(System.getProperty("user.dir"))
            return if (currentDir.name == "ontrack-ui") {
                currentDir.parentFile
            } else {
                currentDir
            }
        }

    private val defaultWeb: File
        get() = File(rootDir, "ontrack-web")

}
