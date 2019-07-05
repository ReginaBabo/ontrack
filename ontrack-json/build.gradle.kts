description = "JSON utilities."

plugins {
    `java-library`
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-core")
    api("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") {
        exclude(group="org.jetbrains.kotlin")
    }
    implementation("org.apache.commons:commons-lang3")
}
