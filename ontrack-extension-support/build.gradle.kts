plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-extension-api"))
    // Extensions do need Spring annotations
    api("org.springframework:spring-context")

    implementation(project(":ontrack-client"))
    implementation(project(":ontrack-ui-support"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.slf4j:slf4j-api")

    // Make sure the following libraries are available for the extension when they need them
    runtimeOnly(project(":ontrack-git"))
    runtimeOnly(project(":ontrack-tx"))
}
