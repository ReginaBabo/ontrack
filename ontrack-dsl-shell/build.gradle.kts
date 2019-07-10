import net.nemerosa.ontrack.gradle.OntrackBoot15Plugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin

description = "DSL Shell for Ontrack."

plugins {
    groovy
    `java-library`
}

apply<SpringBootPlugin>()

dependencies {
    api(project(":ontrack-dsl"))

    implementation("args4j:args4j")
    implementation("org.slf4j:slf4j-api")
    implementation("org.apache.httpcomponents:httpclient")
    implementation("org.apache.httpcomponents:httpmime")
    implementation("org.springframework.boot:spring-boot-starter")
}

/**
 * BOOT2 Workaround waiting for Spring Boot 2
 */

apply<OntrackBoot15Plugin>()

// FIXME bootRepackage {
//    executable = true
//    classifier = 'executable'
//}

// FIXME rootProject.tasks.publicationPackage {
//    from bootRepackage
//}

// FIXME springBoot {
//    mainClass = "net.nemerosa.ontrack.shell.ShellApplication"
//}
