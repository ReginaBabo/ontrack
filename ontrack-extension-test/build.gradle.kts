import net.nemerosa.ontrack.extension.plugin.OntrackExtension
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import org.apache.tools.ant.filters.ReplaceTokens

buildscript {
    repositories {
        mavenLocal() // Important: used for testing
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        val ontrackVersion: String by project
        classpath("net.nemerosa.ontrack:ontrack-extension-plugin:$ontrackVersion")
    }
}

val ontrackVersion: String by project

group = "net.nemerosa.ontrack"
version = ontrackVersion

repositories {
    mavenLocal() // Important: used for testing
    mavenCentral()
}

plugins {
    java
    id("com.bmuschko.docker-remote-api") version "4.10.0"
}

apply(plugin = "ontrack")

configure<OntrackExtension> {
    kotlin()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.16.18")

    // Test for 3rd party dependencies not included in Ontrack core runtime
    compile("org.apache.commons:commons-math3:3.6.1")

    testImplementation("net.nemerosa.ontrack:ontrack-acceptance:$ontrackVersion")
    testImplementation("net.nemerosa.ontrack:ontrack-dsl:$ontrackVersion")
}

val prepareDockerImage by tasks.registering(Copy::class) {
    dependsOn("ontrackDist")
    from("build/dist") {
        include("*.jar")
    }
    from("src/main/docker") {
        include("*.yml")
        include("Dockerfile")
        filter(
                ReplaceTokens::class,
                "tokens" to mapOf(
                        "ontrackVersion" to ontrackVersion
                )
        )
    }
    into("build/docker")
}

val buildDockerImage by tasks.registering(DockerBuildImage::class) {
    dependsOn(prepareDockerImage)
    inputDir.set(file("build/docker"))
    tags.add("nemerosa/ontrack-extension-test:$version")
}

tasks {
    "assemble" {
        dependsOn(buildDockerImage)
    }
}
