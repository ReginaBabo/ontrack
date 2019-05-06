description = "Abstract management of identified jobs."

plugins {
    `java-library`
}

dependencies {
    implementation(project(":ontrack-common"))
    implementation("org.springframework:spring-context")
    implementation("org.slf4j:slf4j-api")
    implementation("io.micrometer:micrometer-core")
    implementation("org.apache.commons:commons-lang3")

    testImplementation(project(":ontrack-test-utils"))
    testImplementation("org.apache.commons:commons-math3")
    testImplementation("com.google.guava:guava")
    testRuntimeOnly("org.slf4j:slf4j-log4j12")
}
