plugins {
    groovy
}

dependencies {

    compile(project(":ontrack-ui-support"))
    compile("com.graphql-java:graphql-java")
    compile("org.springframework:spring-tx")

    testCompile(project(":ontrack-test-utils"))
    testCompile(project(":ontrack-it-utils"))
    testCompile(project(path = ":ontrack-extension-api", configuration = "tests"))
    testCompile(project(path = ":ontrack-model", configuration = "tests"))
    testCompile("org.codehaus.groovy:groovy-all")
    testCompile("org.springframework.boot:spring-boot-starter-test")

    testCompile(project(":ontrack-repository-impl"))
    testCompile(project(":ontrack-extension-general"))

    testRuntime(project(":ontrack-service"))

}

// Exporting the tests

val tests by configurations.creating

val testJar by tasks.registering(Jar::class) {
    classifier = "tests"
    from(sourceSets["test"].output)
}

artifacts {
    add("tests", testJar)
}
