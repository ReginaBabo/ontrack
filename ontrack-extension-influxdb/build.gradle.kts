import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
}

apply<OntrackExtensionPlugin>()

dependencies {
    api("org.influxdb:influxdb-java")
    api(project(":ontrack-extension-support"))

    implementation("org.slf4j:slf4j-api")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation("org.codehaus.groovy:groovy-all")
    testImplementation(project(path = ":ontrack-extension-api", configuration = "tests"))

    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
}
