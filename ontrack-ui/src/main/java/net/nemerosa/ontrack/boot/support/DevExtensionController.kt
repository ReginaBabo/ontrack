package net.nemerosa.ontrack.boot.support

import net.nemerosa.ontrack.common.RunProfile
import net.nemerosa.ontrack.model.exceptions.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import javax.annotation.PostConstruct

/**
 * This controller is activated only for development mode, when the content of resources for the
 * extension is not directly available for refreshing on the classpath.
 */
@Profile(RunProfile.DEV)
@RestController
@RequestMapping("/extension")
class DevExtensionController(
        private val devSettings: DevSettings
) {

    private val log: Logger = LoggerFactory.getLogger(DevExtensionController::class.java)

    @GetMapping("{extension:[a-z]+}/{path:.*}")
    fun serveExtension(
            @PathVariable extension: String,
            @PathVariable path: String
    ): HttpEntity<String> {
        // Gets the root folder
        val root = devSettings.rootDir
        // Gets the path to the extension resource
        val file = File(
                root,
                "ontrack-extension-$extension/src/main/resources/static/extension/$extension/$path"
        )
        // Gets the file content or an error
        return if (file.exists()) {
            val contentType = when {
                path.endsWith(".js") -> MediaType.valueOf("text/javascript")
                path.endsWith(".html") -> MediaType.TEXT_HTML
                path.endsWith(".html") -> MediaType.valueOf("text/css")
                else -> MediaType.TEXT_PLAIN
            }
            val body = file.readText()
            ResponseEntity.ok().contentType(contentType).body(body)
        } else {
            throw DevExtensionNotFoundException(extension, path, file)
        }
    }

    class DevExtensionNotFoundException(
            extension: String,
            path: String,
            file: File
    ) : NotFoundException("Could not service extension file for $extension/$path because no file is present at $file.")

    @PostConstruct
    fun warning() {
        log.warn("""[dev] Running in DEV mode.""")
    }

}