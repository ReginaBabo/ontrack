plugins {
    groovy
    kotlin("jvm") version "1.3.41"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation(gradleApi())
    implementation("gradle.plugin.com.liferay:gradle-plugins-node:4.6.18")
    compile("net.nemerosa.ontrack:ontrack-dsl:2.23.2") {
        exclude(module = "groovy-all")
    }
}
