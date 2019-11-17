package net.nemerosa.ontrack.kdsl.test.app

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File

@Component
@ConfigurationProperties("ontrack.test")
class SpringTestProperties {

    var config = SpringTestConfig()
    var output = TestOutput()

    class TestOutput {
        var dir: File = File("build")
        var name: String = "test.xml"
    }

}