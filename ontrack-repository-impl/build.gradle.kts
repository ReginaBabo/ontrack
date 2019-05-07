plugins {
    `java-library`
}

dependencies {
    implementation(project(":ontrack-database"))
    implementation(project(":ontrack-repository"))
    implementation(project(":ontrack-repository-support"))
    implementation("org.apache.commons:commons-lang3")
    implementation("com.google.guava:guava") // TODO Remove, used only for Nullable
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework:spring-context")
    implementation("org.slf4j:slf4j-api")
    implementation("org.flywaydb:flyway-core")

    testImplementation(project(":ontrack-it-utils"))

    testRuntimeOnly(project(":ontrack-service"))
}
