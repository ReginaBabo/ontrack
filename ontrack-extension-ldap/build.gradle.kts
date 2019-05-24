import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    implementation(project(":ontrack-extension-support"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.security:spring-security-ldap")
    implementation("commons-logging:commons-logging")
    implementation(project(":ontrack-ui-support"))
    implementation("javax.validation:validation-api")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation("org.codehaus.groovy:groovy-all")

    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
}