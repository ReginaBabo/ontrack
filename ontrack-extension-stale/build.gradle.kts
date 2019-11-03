import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    groovy
    `java-library`
}

apply<OntrackExtensionPlugin>()

sourceSets {
    create("dsl")
}

val kotlinVersion: String by project
val dslImplementation by configurations.getting

dependencies {
    implementation(project(":ontrack-extension-support"))
    implementation("org.slf4j:slf4j-api")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation("org.codehaus.groovy:groovy")

    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))

    dslImplementation(project(":ontrack-kdsl-model"))
    dslImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    dslImplementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
}