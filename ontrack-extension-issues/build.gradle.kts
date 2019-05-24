import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    api(project(":ontrack-extension-support"))

    implementation("org.apache.commons:commons-lang3")
    implementation("com.google.guava:guava")
    implementation("org.springframework:spring-context")
    implementation("org.codehaus.groovy:groovy-all")
}

val testJar by tasks.registering(Jar::class) {
    classifier = "tests"
    from(sourceSets["test"].output)
}

tasks["assemble"].dependsOn("testJar")

val tests by configurations.creating

artifacts {
    add("tests", testJar)
}
