plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-model"))
    
    implementation(project(":ontrack-test-utils"))
    implementation(project(":ontrack-extension-support"))
    implementation(project(":ontrack-ui-support"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework:spring-test")
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