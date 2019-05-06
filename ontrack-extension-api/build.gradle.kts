dependencies {
    api(project(":ontrack-model"))
    
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.google.guava:guava")
}

// FIXME Test artifact export
//configurations {
//    testArtifacts
//}
//
//task testJar(type: Jar) {
//    classifier = "tests"
//    from sourceSets.test.output
//}
//
//assemble.dependsOn testJar
//
//artifacts {
//    archives testJar
//    testArtifacts testJar
//}
