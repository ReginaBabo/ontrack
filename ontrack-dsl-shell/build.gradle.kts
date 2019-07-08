import org.springframework.boot.gradle.plugin.SpringBootPlugin

description = "DSL Shell for Ontrack."

plugins {
    groovy
}

apply<SpringBootPlugin>()

dependencies {
    implementation("args4j:args4j")
    implementation("org.slf4j:slf4j-api")
    implementation("org.apache.httpcomponents:httpclient")
    implementation("org.apache.httpcomponents:httpmime")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":ontrack-dsl"))
}

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
