import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    api(project(":ontrack-git"))
    api(project(":ontrack-json"))
    api(project(":ontrack-extension-scm"))
    api("org.springframework:spring-tx")

    implementation(project(":ontrack-ui-graphql"))
    implementation(project(":ontrack-ui-support"))
    implementation(project(":ontrack-tx"))
    implementation(project(":ontrack-repository-support"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.google.guava:guava")
    implementation("org.apache.commons:commons-lang3")
    implementation("commons-io:commons-io")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation(project(path = ":ontrack-extension-api", configuration = "tests"))
    testImplementation(project(path = ":ontrack-extension-issues", configuration = "tests"))
    testImplementation(project(path = ":ontrack-ui-graphql", configuration = "tests"))
    testImplementation("org.codehaus.groovy:groovy-all")
    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
    testRuntimeOnly("org.springframework.boot:spring-boot-starter-web")
}
