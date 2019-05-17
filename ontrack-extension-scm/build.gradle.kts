import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

plugins {
    `java-library`
    groovy
}

dependencies {
    api(project(":ontrack-extension-support"))
    api(project(":ontrack-extension-issues"))
    api(project(":ontrack-ui-support"))

    implementation("org.apache.commons:commons-lang3")
    implementation("com.google.guava:guava") // TODO Remove, used only for Nullable

    testImplementation("org.codehaus.groovy:groovy-all")
    testImplementation(project(":ontrack-it-utils"))
    testRuntimeOnly(project(":ontrack-service"))
    testRuntimeOnly(project(":ontrack-repository-impl"))
}
