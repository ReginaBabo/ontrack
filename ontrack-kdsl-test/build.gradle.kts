import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `java-library`
}

apply(plugin = "org.springframework.boot")
apply(plugin = "com.bmuschko.docker-remote-api")

description = "Tests for Ontrack KDSL tests, including for core extensions."

dependencies {
    testImplementation(project(":ontrack-test-support"))
    testImplementation(project(":ontrack-test-engine"))
    testImplementation(project(":ontrack-kdsl-model"))
    testImplementation("org.springframework.boot:spring-boot-starter")

    testImplementation(project(":ontrack-extension-stale", "dslConfig"))
}


val bootJar = tasks.getByName<BootJar>("bootJar") {
    bootInf {
        from(sourceSets["test"].output)
        into("classes")
    }
    classpath(configurations.named("testRuntimeClasspath"))
    mainClassName = "net.nemerosa.ontrack.kdsl.test.KDSLAppKt"
}

val normaliseJar by tasks.registering(Copy::class) {
    dependsOn(bootJar)
    from("$buildDir/libs/")
    include("ontrack-kdsl-test-$version.jar")
    into("$buildDir/libs/")
    rename("ontrack-kdsl-test-$version.jar", "ontrack-kdsl-test.jar")
}

val dockerPrepareEnv by tasks.registering(Copy::class) {
    dependsOn(normaliseJar)
    from("${buildDir}/libs/ontrack-kdsl-test.jar")
    into("${projectDir}/src/main/docker")
}

tasks.named("assemble") {
    dependsOn(normaliseJar)
}

val dockerBuild by tasks.registering(DockerBuildImage::class) {
    dependsOn(dockerPrepareEnv)
    inputDir.set(file("src/main/docker"))
    tags.add("nemerosa/ontrack-kdsl-test:$version")
    tags.add("nemerosa/ontrack-kdsl-test:latest")
}

rootProject.tasks.named<Zip>("publicationPackage") {
    from(bootJar)
}
