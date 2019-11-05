dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter")
    testImplementation(project(":ontrack-bdd-engine"))
    testImplementation(project(":ontrack-kdsl-model"))

    testImplementation(project(":ontrack-extension-stale", "dslConfig"))
}
