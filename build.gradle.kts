import com.avast.gradle.dockercompose.ComposeExtension
import com.avast.gradle.dockercompose.tasks.ComposeUp
import net.nemerosa.versioning.VersioningExtension
import net.nemerosa.versioning.VersioningPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
    id("nebula.os-package") version "2.2.6"
    id("org.sonarqube") version "2.5"
    id("com.avast.gradle.docker-compose") version "0.9.4"
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
            archiveClassifier.set("sources")
            // FIXME from sourceSets.main.allSource
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

// FIXME apply from: "gradle/packaging.gradle"

/**
 * Packaging for OS
 */

// FIXME apply from: "gradle/os-packaging.gradle"

/**
 * Docker tasks
 */

// FIXME apply from: "gradle/docker.gradle"

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
