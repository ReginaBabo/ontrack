import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    groovy
    `java-library`
}

apply<OntrackExtensionPlugin>()

dependencies {
    api(project(":ontrack-extension-scm"))
    api(project(":ontrack-git"))
    api(project(":ontrack-tx"))
    api(project(":ontrack-json"))

    implementation(project(":ontrack-ui-graphql"))
    implementation(project(":ontrack-repository-support"))
    implementation("org.springframework:spring-tx")
    implementation("commons-io:commons-io")
    implementation("org.apache.commons:commons-lang3")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation(project(path = ":ontrack-extension-api", configuration = "tests"))
    testImplementation(project(path = ":ontrack-extension-issues", configuration = "tests"))
    testImplementation(project(":ontrack-extension-elasticsearch"))
    testImplementation(project(path = ":ontrack-ui-graphql", configuration = "tests"))
    testImplementation("org.codehaus.groovy:groovy")
    
    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
    testRuntimeOnly("org.springframework.boot:spring-boot-starter-web")
}
