import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    groovy
    `java-library`
}

apply<OntrackExtensionPlugin>()

dependencies {
    implementation(project(":ontrack-extension-issues"))
    implementation(project(":ontrack-ui-support"))
    implementation(project(":ontrack-client"))
    implementation(project(":ontrack-tx"))
    implementation("com.google.guava:guava") // TODO Remove, used only for Nullable
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework:spring-tx")
    implementation("org.apache.commons:commons-lang3")

    testImplementation("org.codehaus.groovy:groovy-all")
    testImplementation(project(":ontrack-it-utils"))
    testImplementation(project(path = ":ontrack-ui-graphql", configuration = "tests"))

    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
}