dependencies {
    compile(project(":ontrack-model"))
    compile("org.springframework.boot:spring-boot-starter-actuator")
}

tasks.register<Jar>("testJar") {
    classifier = "tests"
    from(sourceSets.getByName("test").output)
}

tasks.named<Task>("assemble") {
    dependsOn("testJar")
}
