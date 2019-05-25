import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

apply<OntrackExtensionPlugin>()

dependencies {
    implementation(project(":ontrack-extension-issues"))
    implementation(project(":ontrack-ui-support"))
    implementation("org.apache.commons:commons-lang3")
    implementation("com.google.guava:guava")

    testImplementation(project(path = ":ontrack-extension-issues", configuration = "tests"))
    testImplementation("org.codehaus.groovy:groovy-all")
}
