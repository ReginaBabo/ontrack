import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    implementation(project(":ontrack-extension-git"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.eclipse.mylyn.github:org.eclipse.egit.github.core:2.1.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.google.guava:guava")

    testImplementation(project(":ontrack-test-utils"))
    testImplementation(project(":ontrack-it-utils"))
    testImplementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation(project(path = ":ontrack-extension-issues", configuration = "tests"))

    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
}