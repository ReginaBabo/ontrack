plugins {
    groovy
}

val tests by configurations.creating

dependencies {
    compile(project(":ontrack-extension-support"))
    compile("org.codehaus.groovy:groovy-all")
}

val testJar by tasks.registering(Jar::class) {
    baseName = "${project.name}-test"
    from(sourceSets["test"].output)
}

artifacts {
    add("tests", testJar)
}
