import com.avast.gradle.dockercompose.ComposeExtension
import com.avast.gradle.dockercompose.tasks.ComposeUp
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.netflix.gradle.plugins.deb.Deb
import com.netflix.gradle.plugins.packaging.SystemPackagingTask
import com.netflix.gradle.plugins.rpm.Rpm
import net.nemerosa.versioning.VersioningExtension
import net.nemerosa.versioning.VersioningPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.redline_rpm.header.Os
import java.lang.Thread.sleep

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        val kotlinVersion: String by project
        val springBootVersion: String by project
        classpath("com.netflix.nebula:gradle-aggregate-javadocs-plugin:3.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    }
}

/**
 * Plugins
 */

plugins {
    id("net.nemerosa.versioning") version "2.8.2" apply false
    id("nebula.deb") version "6.2.1"
    id("nebula.rpm") version "6.2.1"
    id("org.sonarqube") version "2.5"
    id("com.avast.gradle.docker-compose") version "0.9.4"
    id("com.bmuschko.docker-remote-api") version "4.10.0"
}

/**
 * Build properties
 */

val micrometerVersion: String by project
val springBootVersion: String by project

/**
 * Meta information
 */

apply<VersioningPlugin>()
version = extensions.getByType<VersioningExtension>().info.display

allprojects {
    group = "net.nemerosa.ontrack"
}

subprojects {
    version = rootProject.version
}

/**
 * Resolution
 */

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}


/**
 * Integration test environment
 */

val itProject: String by project
val itJdbcUrl: String by project
val itJdbcUsername: String by project
val itJdbcPassword: String by project
val itJdbcWait: String by project

// Pre-integration tests: starting Postgresql

val preIntegrationTest by tasks.registering {
    dependsOn("integrationTestComposeUp")
    doLast {
        val postgresPort = tasks.named<ComposeUp>("integrationTestComposeUp").get().servicesInfos["db"]?.firstContainer?.tcpPort
        val jdbcUrl = "jdbc:postgresql://localhost:$postgresPort/ontrack"
        val itJdbcUrl: String by rootProject.extra(jdbcUrl)
        logger.info("JDBC URL = $itJdbcUrl")
        logger.info("JDBC Wait $itJdbcWait seconds for DB container to be ready...")
        sleep((itJdbcWait.toLong()) * 1000)
    }
}

// Integration test Compose setup

configure<ComposeExtension> {
    createNested("integrationTest").apply {
        useComposeFiles = listOf("compose/docker-compose-it.yml")
        projectName = itProject
    }
}

// Post-integration tests: stopping Postgresql

val postIntegrationTest by tasks.registering {
    dependsOn("integrationTestComposeDown")
}

/**
 * Java projects
 */

val javaProjects = subprojects.filter {
    it.path != ":ontrack-web"
}

configure(javaProjects) p@{

    /**
     * For all Java projects
     */

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "net.nemerosa.versioning")

    apply(plugin = "kotlin-platform-jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    /**
     * Dependency management
     */

    dependencies {
        // Spring Boot managed dependencies
        "implementation"(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        // Constraints
        constraints {
            // Commons
            "implementation"("commons-io:commons-io:2.6")
            "implementation"("org.apache.commons:commons-lang3:3.8.1")
            "implementation"("org.apache.commons:commons-text:1.6")
            // InfluxDB
            "implementation"("org.influxdb:influxdb-java:2.14")
            // Metrics
            "implementation"("io.micrometer:micrometer-core:$micrometerVersion")
            "implementation"("io.micrometer:micrometer-spring-legacy:$micrometerVersion")
            "implementation"("io.micrometer:micrometer-registry-influx:$micrometerVersion")
            "implementation"("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")
            // Misc.
            "implementation"("args4j:args4j:2.33")
            "implementation"("com.google.guava:guava:27.0.1-jre")
            "implementation"("org.kohsuke:groovy-sandbox:1.19")
            "implementation"("com.graphql-java:graphql-java:11.0")
            "implementation"("org.jgrapht:jgrapht-core:1.3.0")
            "implementation"("org.apache.commons:commons-math3:3.6.1")
            // Overrides from Spring Boot
            "implementation"("org.flywaydb:flyway-core:4.2.0")
        }
        // Kotlin
        "implementation"(kotlin("stdlib-jdk8"))
        "implementation"(kotlin("reflect"))
        // Testing
        "testImplementation"("junit:junit")
        "testImplementation"("org.mockito:mockito-core")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test")
        // Lombok (TODO Replace with Kotlin)
        "compileOnly"("org.projectlombok:lombok:1.18.8")
        "testCompileOnly"("org.projectlombok:lombok:1.18.8")
        "annotationProcessor"("org.projectlombok:lombok:1.18.8")
        "testAnnotationProcessor"("org.projectlombok:lombok:1.18.8")
    }

    // Kotlin options

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    // Unit testing
    tasks.named<Test>("test") {
        include("**/*Test.class")
    }

    // Integration tests
    tasks.register<Test>("integrationTest") {
        include("**/*IT.class")
        minHeapSize = "512m"
        maxHeapSize = "1024m"
        dependsOn(preIntegrationTest)
        finalizedBy(postIntegrationTest)
        /**
         * Sets the JDBC URL
         */
        doFirst {
            val jdbcUrl = rootProject.extra["itJdbcUrl"] as String
            println("Setting JDBC URL for IT: $jdbcUrl")
            systemProperty("it.jdbc.url", jdbcUrl)
            systemProperty("it.jdbc.user", itJdbcUsername)
            systemProperty("it.jdbc.password", itJdbcPassword)
        }
    }

    // Javadoc

    if (hasProperty("documentation")) {

        tasks.register<Jar>("javadocJar") {
            archiveClassifier.set("javadoc")
            from("javadoc")
        }

        // Sources

        tasks.register<Jar>("sourcesJar") {
            dependsOn(JavaPlugin.CLASSES_TASK_NAME)
            archiveClassifier.set("sources")
            from(project.the<SourceSetContainer>()["main"].allSource)
        }

    }

    // POM file

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenCustom") {
                from(components["java"])
                if (hasProperty("documentation")) {
                    artifact(tasks["sourcesJar"])
                    artifact(tasks["javadocJar"])
                }
                pom {
                    name.set(this@p.name)
                    description.set(this@p.description)
                    url.set("http://nemerosa.github.io/ontrack")
                    licenses {
                        license {
                            name.set("The MIT License (MIT)")
                            url.set("http://opensource.org/licenses/MIT")
                            distribution.set("repo")
                        }
                    }
                    scm {
                        connection.set("scm:git://github.com/nemerosa/ontrack")
                        developerConnection.set("scm:git://github.com/nemerosa/ontrack")
                        url.set("https://github.com/nemerosa/ontrack/")
                    }
                    developers {
                        developer {
                            id.set("dcoraboeuf")
                            name.set("Damien Coraboeuf")
                            email.set("damien.coraboeuf@gmail.com")
                        }
                    }
                }
            }
        }
    }
}


//configure(coreProjects) {
//
//    // Acceptance tests
//    FIXME task acceptanceTest(type: Test, dependsOn: "integrationTest") {
//        include "**/ACC*.class"
//        ignoreFailures true
//        reports {
//            html.enabled = false
//        }
//    }
//
//}

/**
 * Packaging for delivery
 */

// Ontrack descriptor

val deliveryDescriptor by tasks.registering {
    val output = project.file("build/ontrack.properties")
    extra["output"] = output
    doLast {
        // Directories
        output.parentFile.mkdirs()
        output.writeText("# Ontrack properties\n")
        // Version information
        val version = rootProject.extensions.getByType<VersioningExtension>().info
        output.appendText("# Version information\n")
        output.appendText("VERSION_BUILD = ${version.build}\n")
        output.appendText("VERSION_BRANCH = ${version.branch}\n")
        output.appendText("VERSION_BASE = ${version.base}\n")
        output.appendText("VERSION_BRANCHID = ${version.branchId}\n")
        output.appendText("VERSION_BRANCHTYPE = ${version.branchType}\n")
        output.appendText("VERSION_COMMIT = ${version.commit}\n")
        output.appendText("VERSION_DISPLAY = ${version.display}\n")
        output.appendText("VERSION_FULL = ${version.full}\n")
        output.appendText("VERSION_SCM = ${version.scm}\n")
        // Modules
        output.appendText("# Comma-separated list of modules\n")
        output.appendText("MODULES = ${rootProject.subprojects.filter { it.tasks.findByName("jar") != null }.joinToString(",") { it.name }}\n")
    }
}

apply(from = "gradle/packaging.gradle.kts")

/**
 * Packaging for OS
 */

// The package version does not accept versions like the ones generated
// from the Versioning plugin for the feature branches for example.

val packageVersion = if (version.toString().matches("\\d+\\.\\d+\\.\\d+".toRegex())) {
    version.toString().replace("[^0-9\\.-_]".toRegex(), "")
} else {
    "0.0.0"
}

println("Package version = $packageVersion")

val debPackage by tasks.registering(Deb::class) {
    dependsOn(":ontrack-ui:bootRepackage")

    link("/etc/init.d/ontrack", "/opt/ontrack/bin/ontrack.sh")
}

val rpmPackage by tasks.registering(Rpm::class) {
    dependsOn(":ontrack-ui:bootRepackage")

    user = "ontrack"
    link("/etc/init.d/ontrack", "/opt/ontrack/bin/ontrack.sh")
}

tasks.withType(SystemPackagingTask::class) {

    packageName = "ontrack"
    release = "1"
    version = packageVersion
    os = Os.LINUX // only applied to RPM

    preInstall("gradle/os-package/preInstall.sh")
    postInstall("gradle/os-package/postInstall.sh")

    from(project(":ontrack-ui").file("build/libs"), closureOf<CopySpec> {
        include("ontrack-ui-${project.version}.jar")
        into("/opt/ontrack/lib")
        rename(".*", "ontrack.jar")
    })

    from("gradle/os-package", closureOf<CopySpec> {
        include("ontrack.sh")
        into("/opt/ontrack/bin")
        fileMode = 0x168 // 0550
    })
}

val osPackages by tasks.registering {
    dependsOn(rpmPackage)
    dependsOn(debPackage)
}

/**
 * Docker tasks
 */

val dockerPrepareEnv by tasks.registering(Copy::class) {
    from("ontrack-ui/build/libs")
    include("*.jar")
    exclude("*-javadoc.jar")
    exclude("*-sources.jar")
    into(project.file("docker"))
    rename(".*", "ontrack.jar")
}

val dockerBuild by tasks.registering(DockerBuildImage::class) {
    inputDir.set(file("docker"))
    tags.add("nemerosa/ontrack:$version")
    tags.add("nemerosa/ontrack:latest")
}

/**
 * FIXME Acceptance tasks
 */

//dockerCompose {
//    ci {
//        useComposeFiles = ["compose/docker-compose-ci.yml"]
//        projectName = "ci"
//        captureContainersOutputToFiles = "${project.buildDir}/ci-logs"
//        waitForTcpPorts = false
//    }
//}
//
//task ciAcceptanceTest(type: RemoteAcceptanceTest) {
//    dependsOn "ciComposeUp"
//    doFirst {
//        def ontrackPort = dockerCompose.ci.servicesInfos.ontrack."ontrack_1".ports.get(8080) as int
//        acceptanceUrl = { "http://localhost:${ontrackPort}" }
//    }
//    disableSsl = true
//    acceptanceTimeout = 300
//    acceptanceImplicitWait = 30
//    finalizedBy ciComposeDown
//}

/**
 * Development tasks
 */

dockerCompose {
    createNested("devInit").apply {
        useComposeFiles = listOf("compose/docker-compose-dev.yml")
    }
}

val devStart by tasks.registering {
    dependsOn("devInitComposeUp")
}

val devStop by tasks.registering {
    dependsOn("devInitComposeDown")
}
