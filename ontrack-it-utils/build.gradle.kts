plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-model"))
    api(project(":ontrack-test-utils"))
    api("org.springframework:spring-test")
    api("org.springframework:spring-jdbc")

    implementation(project(":ontrack-extension-support"))
    implementation(project(":ontrack-ui-support"))
    implementation("com.google.guava:guava")
    implementation("org.springframework:spring-context")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.slf4j:slf4j-api")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-spring-legacy")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("io.dropwizard.metrics:metrics-core")
    runtimeOnly("org.hibernate:hibernate-validator")
}