plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-json"))
    api(project(":ontrack-test-support"))

    implementation("org.apache.commons:commons-lang3")
    implementation("commons-io:commons-io")
}
