dependencies {
    api(project(":ontrack-model"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.google.guava:guava")
}

tasks.register<Jar>("testJar") {
    classifier = "tests"
    from(sourceSets["test"].allJava)
}

tasks["assemble"].dependsOn("testJar")
