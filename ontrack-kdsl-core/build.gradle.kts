plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-kdsl-client"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}
