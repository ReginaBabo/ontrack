plugins {
    `java-library`
}

val kotlinVersion: String by project

dependencies {
    api(project(":ontrack-json"))
    api("com.fasterxml.jackson.core:jackson-databind")
    api("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    api("junit:junit")

    implementation("commons-io:commons-io")
    implementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.apache.commons:commons-lang3")

}
