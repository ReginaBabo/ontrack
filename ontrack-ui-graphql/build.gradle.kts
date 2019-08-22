plugins {
    `java-library`
    groovy
}

dependencies {
    api("com.graphql-java:graphql-java")

    implementation(project(":ontrack-ui-support"))
    implementation("org.springframework:spring-tx")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-core")
    implementation("com.google.guava:guava") // TODO: remove, used only for Nullable

    testImplementation(project(":ontrack-test-utils"))
    testImplementation(project(":ontrack-it-utils"))
    testImplementation(project(path = ":ontrack-extension-api", configuration = "tests"))
    testImplementation(project(path = ":ontrack-model", configuration = "tests"))
    testImplementation("org.codehaus.groovy:groovy-all")
    testImplementation("org.apache.tomcat:tomcat-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(project(":ontrack-repository-impl"))
    testImplementation(project(":ontrack-extension-general"))

    testRuntimeOnly(project(":ontrack-service"))

}

val testJar by tasks.registering(Jar::class) {
    classifier = "tests"
    from(sourceSets["test"].output)
}

configure<PublishingExtension> {
    publications {
        maybeCreate<MavenPublication>("mavenCustom").artifact(tasks["testJar"])
    }
}

tasks["assemble"].dependsOn("testJar")

val tests by configurations.creating

artifacts {
    add("tests", testJar)
}
