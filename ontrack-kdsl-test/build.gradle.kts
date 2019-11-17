plugins {
    `java-library`
}

apply(plugin = "org.springframework.boot")
apply(plugin = "com.bmuschko.docker-remote-api")

description = "Tests for Ontrack KDSL tests, including for core extensions."

dependencies {
    testImplementation(project(":ontrack-test-support"))
    testImplementation(project(":ontrack-test-engine"))
    testImplementation(project(":ontrack-kdsl-model"))
    testImplementation("org.springframework.boot:spring-boot-starter")

    testImplementation(project(":ontrack-extension-stale", "dslConfig"))
}
