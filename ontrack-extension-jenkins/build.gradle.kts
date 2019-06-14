import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    implementation(project(":ontrack-extension-support"))
    implementation(project(":ontrack-ui-support"))
    implementation(project(":ontrack-client"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework:spring-tx")

    testImplementation("org.codehaus.groovy:groovy-all")
    testImplementation(project(":ontrack-test-utils"))

}