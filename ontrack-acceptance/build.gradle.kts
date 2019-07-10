import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import net.nemerosa.ontrack.gradle.OntrackBoot15Plugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.repackage.RepackageTask

plugins {
    groovy
}

apply<SpringBootPlugin>()
apply(plugin = "com.bmuschko.docker-remote-api")

dependencies {
    testImplementation(project(":ontrack-client"))
    testImplementation(project(":ontrack-dsl"))
    testImplementation(project(":ontrack-dsl-shell"))
    testImplementation(project(":ontrack-test-utils"))
    testImplementation("org.apache.commons:commons-lang3")
    testImplementation("commons-io:commons-io")
    testImplementation("org.codehaus.groovy:groovy-all")
    testImplementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.influxdb:influxdb-java")

    testImplementation("org.seleniumhq.selenium:selenium-java")
    testImplementation("org.seleniumhq.selenium:selenium-support")
}

/**
 * BOOT2 Workaround waiting for Spring Boot 2
 */

apply<OntrackBoot15Plugin>()

/**
 * Packaging
 */

tasks.named<Jar>("jar") {
    from(sourceSets["test"].output)
}

/**
 * Spring boot packaging
 */

val bootRepackage by tasks.named<RepackageTask>("bootRepackage") {
    setCustomConfiguration("testRuntimeClasspath")
    mainClass = "net.nemerosa.ontrack.acceptance.boot.Start"
}

val normaliseJar by tasks.registering(Copy::class) {
    dependsOn("bootRepackage")
    from("$buildDir/libs/")
    include("ontrack-acceptance-$version.jar")
    into("$buildDir/libs/")
    rename("ontrack-acceptance-$version.jar", "ontrack-acceptance.jar")
}

tasks.named("assemble") {
    dependsOn(normaliseJar)
}

/**
 * Acceptance Docker image
 */

val acceptanceDockerPrepareEnv by tasks.registering(Copy::class) {
    dependsOn(normaliseJar)
    from("$buildDir/libs/ontrack-acceptance.jar")
    into("$projectDir/src/main/docker")
}

val dockerBuild by tasks.registering(DockerBuildImage::class) {
    dependsOn(acceptanceDockerPrepareEnv)
    inputDir.set(file("src/main/docker"))
    tags.add("nemerosa/ontrack-acceptance:$version")
    tags.add("nemerosa/ontrack-acceptance:latest")
}

rootProject.tasks.named<Zip>("publicationPackage") {
    from(bootRepackage)
}

/**
 * Local test definitions
 */

val ontrackUrl: String by project
val ontrackJvmOptions: String by project
val ontrackImplicitWait: String by project

val acceptanceTest by tasks.registering(Test::class) {
    include("**/ACC*.class")
    ignoreFailures = true
    systemProperties(
            mapOf(
                    "ontrack.url" to ontrackUrl,
                    "ontrack.implicitWait" to ontrackImplicitWait
            )
    )
    outputs.upToDateWhen { false }  // Always run tests
}

// Disable unit tests (none in this project)

tasks.named<Test>("test") {
    enabled = false
}
