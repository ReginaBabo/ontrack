plugins {
    `java-library`
}

apply(plugin = "org.springframework.boot")
apply(plugin = "com.bmuschko.docker-remote-api")

description = "Tests for Ontrack KDSL tests, including for core extensions."

dependencies {
    implementation(project(":ontrack-test-support"))
    implementation(project(":ontrack-kdsl-model"))

    implementation(project(":ontrack-extension-stale", "dslConfig"))
}
