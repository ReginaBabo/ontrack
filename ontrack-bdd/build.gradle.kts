import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

plugins {
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

apply(plugin = "com.bmuschko.docker-remote-api")

description = "BDD scenarios for Ontrack E2E tests, including for core extensions."

dependencies {
    implementation(project(":ontrack-bdd-engine"))
    implementation(project(":ontrack-bdd-definitions"))

    implementation(project(":ontrack-extension-stale", "bddConfig"))
}

/**
 * Packaging
 */

val shadowJar by tasks.named<ShadowJar>("shadowJar") {
    isZip64 = true
    archiveClassifier.set("app")
    manifest {
        attributes(
                "Main-Class" to "net.nemerosa.ontrack.bdd.BDDApp"
        )
    }
}

tasks.named("assemble") {
    dependsOn(shadowJar)
}

val normaliseJar by tasks.registering(Copy::class) {
    dependsOn(shadowJar)
    from("$buildDir/libs/")
    include("ontrack-bdd-$version-app.jar")
    into("$buildDir/libs/")
    rename("ontrack-bdd-$version-app.jar", "ontrack-bdd-app.jar")
}

tasks.named("assemble") {
    dependsOn(normaliseJar)
}

rootProject.tasks.named<Zip>("publicationPackage") {
    from(shadowJar)
}

// Docker packaging

val bddDockerPrepareEnv by tasks.registering(Copy::class) {
    dependsOn(normaliseJar)
    from("${buildDir}/libs/ontrack-bdd-app.jar")
    into("${projectDir}/src/main/docker")
}

val dockerBuild by tasks.registering(DockerBuildImage::class) {
    dependsOn(bddDockerPrepareEnv)
    inputDir.set(file("src/main/docker"))
    tags.add("nemerosa/ontrack-bdd:$version")
    tags.add("nemerosa/ontrack-bdd:latest")
}

// Disable unit tests (none in this project)

tasks.named<Test>("test") {
    enabled = false
}

configure<PublishingExtension> {
    publications {
        named<MavenPublication>("mavenCustom") {
            setArtifacts(listOf(shadowJar))
        }
    }
}
