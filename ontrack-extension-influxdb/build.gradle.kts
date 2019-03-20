import net.nemerosa.ontrack.gradle.extension.OntrackExtensionPlugin

apply<OntrackExtensionPlugin>()

dependencies {
    compile(project(":ontrack-extension-support"))
    compile("org.influxdb:influxdb-java")

    testCompile(project(":ontrack-it-utils"))
    testCompile("org.codehaus.groovy:groovy-all")
    testCompile(project(path = ":ontrack-extension-api", configuration = "tests"))

    testRuntime(project(":ontrack-service"))
    testRuntime(project(":ontrack-repository-impl"))
}
