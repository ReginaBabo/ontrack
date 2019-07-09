import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.repackage.RepackageTask

plugins {
    groovy
}

apply<SpringBootPlugin>()

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

// FIXME task acceptanceDockerPrepareEnv(type: Copy) {
//    dependsOn normaliseJar
//    from "${buildDir}/libs/ontrack-acceptance.jar"
//    into "${projectDir}/src/main/docker"
//}
//
// FIXME assemble.dependsOn normaliseJar

// FIXME Acceptance Docker image
//task dockerBuild(type: Exec, dependsOn: acceptanceDockerPrepareEnv) {
//    executable 'docker'
//    args = [
//            'build',
//            '--tag',
//            "nemerosa/ontrack-acceptance:${versioning.info.display}",
//            project.file('src/main/docker')
//    ]
//}
//
//task dockerLatest(type: Exec, dependsOn: dockerBuild) {
//    executable 'docker'
//    args = [
//            'tag',
//            "nemerosa/ontrack-acceptance:${versioning.info.display}",
//            "nemerosa/ontrack-acceptance:latest",
//    ]
//}

// FIXME rootProject.tasks.publicationPackage {
//    from bootRepackage
//}

/**
 * FIXME Local test definitions
 */

//ext {
//    ontrackUrl = 'http://localhost:8080'
//    ontrackJvmOptions = project.hasProperty('ontrackJvmOptions') ? project.ontrackJvmOptions : '-Xmx256m'
//}

// FIXME acceptanceTest {
//    outputs.upToDateWhen { false }  // Always run tests
//    systemProperty 'ontrack.url', ontrackUrl
//    systemProperty 'ontrack.implicitWait', project.hasProperty('ontrackImplicitWait') ? ontrackImplicitWait : 5
//}

// FIXME Disable unit tests (none in this project)
//test {
//    enabled = false
//}

// FIXME Disable normal JAR creation

//jar {
//    enabled = false
//}

// FIXME publishing {
//    publications {
//        mavenCustom(MavenPublication) {
//            // Clears all previous artifacts defined in root `build.gradle`
//            artifacts = [testJar]
//        }
//    }
//}
