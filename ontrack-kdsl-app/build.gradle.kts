plugins {
    `java-library`
}

apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":ontrack-kdsl-runner"))
}
