import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "BDD scenarios for Ontrack E2E tests, including for core extensions."

apply(plugin = "org.springframework.boot")

dependencies {
    testImplementation(project(":ontrack-bdd-engine"))
    testImplementation(project(":ontrack-bdd-definitions"))

    testImplementation(project(":ontrack-extension-stale", "bddConfig"))
}
/**
 * Packaging
 */

val bootJar = tasks.getByName<BootJar>("bootJar") {
    bootInf {
        from(sourceSets["test"].output)
        into("classes")
    }
    classpath(configurations.named("testRuntimeClasspath"))
    mainClassName = "net.nemerosa.ontrack.bdd.BDDApp"
    // Cucumber backend detection needs this to be loaded
    requiresUnpack("**/cucumber-java-*.jar")
    // Required for the detection of feature files
    requiresUnpack("**/ontrack-bdd-definitions-*.jar")
}

val normaliseJar by tasks.registering(Copy::class) {
    dependsOn(bootJar)
    from("$buildDir/libs/")
    include("ontrack-bdd-$version.jar")
    into("$buildDir/libs/")
    rename("ontrack-bdd-$version.jar", "ontrack-bdd.jar")
}

tasks.named("assemble") {
    dependsOn(normaliseJar)
}

rootProject.tasks.named<Zip>("publicationPackage") {
    from(bootJar)
}

// Disable unit tests (none in this project)

tasks.named<Test>("test") {
    enabled = false
}

configure<PublishingExtension> {
    publications {
        named<MavenPublication>("mavenCustom") {
            setArtifacts(listOf(bootJar))
        }
    }
}
