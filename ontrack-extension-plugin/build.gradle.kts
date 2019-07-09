import org.apache.tools.ant.filters.ReplaceTokens

description = "Gradle plugin to create an Ontrack extension."

plugins {
    groovy
}

repositories {
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

val kotlinVersion: String by project

dependencies {
    implementation(gradleApi())
    implementation("gradle.plugin.com.liferay:gradle-plugins-node:4.3.3")
    "implementation"(kotlin("gradle-plugin"))
    "implementation"(kotlin("allopen"))
}

tasks.named<ProcessResources>("processResources") {
    filter(
            ReplaceTokens::class,
            "tokens" to mapOf(
                    "version" to version as String,
                    "kotlinVersion" to kotlinVersion
            )
    )
}
