import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    implementation(project(":ontrack-extension-support"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.vault:spring-vault-core:1.1.1.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation("org.codehaus.groovy:groovy-all")
    testImplementation(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
}
