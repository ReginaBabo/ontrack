plugins {
    `java-library`
}

dependencies {
    implementation(project(":ontrack-model"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-jdbc")
    implementation("org.flywaydb:flyway-core")
    implementation("org.slf4j:slf4j-log4j12")
}
