import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    implementation(project(":ontrack-extension-scm"))
    implementation(project(":ontrack-repository-support"))
    implementation(project(":ontrack-ui-support"))
    implementation(project(":ontrack-ui-graphql"))
    implementation(project(":ontrack-tx"))
    implementation(project(":ontrack-job"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.google.guava:guava") // TODO Remove, used only for Nullable
    implementation("org.apache.commons:commons-lang3")
    implementation("commons-io:commons-io")
    implementation("org.springframework:spring-tx")

    implementation("org.tmatesoft.svnkit:svnkit:1.8.12")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation(project(path = ":ontrack-extension-issues", configuration = "tests"))
    testImplementation(project(path = ":ontrack-extension-api", configuration = "tests"))
    testImplementation(project(path = ":ontrack-ui-graphql", configuration = "tests"))
    testImplementation("org.codehaus.groovy:groovy-all")

    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
}