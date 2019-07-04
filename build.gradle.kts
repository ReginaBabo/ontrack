import com.avast.gradle.dockercompose.ComposeExtension
import com.avast.gradle.dockercompose.tasks.ComposeUp
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.Thread.sleep

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.netflix.nebula:gradle-aggregate-javadocs-plugin:3.0.1")
        // FIXME Reuse kotlinVersion from gradle.properties
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.11")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.3.11")
    }
}

/**
 * Build properties
 */

val micrometerVersion: String by project
val springBootVersion: String by project
val kotlinVersion: String by project

/**
 * Plugins
 */

plugins {
    id("net.nemerosa.versioning") version "2.8.2"
    id("nebula.os-package") version "2.2.6"
    id("org.sonarqube") version "2.5"
    id("com.avast.gradle.docker-compose") version "0.9.4"
    // FIXME Reuse springBootVersion
    id("org.springframework.boot") version "1.5.18.RELEASE" apply false
}

/**
 * Meta information
 */

allprojects p@{
    group = "net.nemerosa.ontrack"
    // FIXME version = this@p.versioning.info.display
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
            // Overrides from Spring Boot
//            dependency "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8"
//            dependency "org.postgresql:postgresql:9.4.1208"
            "implementation"("org.flywaydb:flyway-core:4.2.0")
        }
        // Kotlin
        "implementation"(kotlin("stdlib-jdk8", version = kotlinVersion))
        "implementation"(kotlin("reflect", version = kotlinVersion))
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
//    dependencyManagement {
//        imports {
//            mavenBom("org.springframework.boot:spring-boot-dependencies:${springBootVersion}")
//        }
//        dependencies {
//            dependency "net.jodah:failsafe:1.1.1"
//            dependency "commons-logging:commons-logging:1.2"
//            dependency "org.apache.commons:commons-math3:3.6.1"
//            dependency "net.sf.dbinit:dbinit:1.4.0"
//            dependency "org.jgrapht:jgrapht-core:1.3.0"
//            dependency "org.kohsuke:groovy-sandbox:1.19"
//            dependency "com.graphql-java:graphql-java:11.0"
//            dependency "org.jetbrains.kotlin:kotlin-test:${kotlinVersion}"
//        }
//    }
//
//    compileKotlin {
//        kotlinOptions.jvmTarget = "1.8"
//    }
//    compileTestKotlin {
//        kotlinOptions.jvmTarget = "1.8"
//    }
//
//    // Unit tests run with the `test` task
//    test {
//        include "**/*Test.class"
//        reports {
//            html.enabled = false
//        }
//    }
//
//    // Integration tests
//    task integrationTest(type: Test, dependsOn: "test") {
//        include "**/*IT.class"
//        minHeapSize = "512m"
//        maxHeapSize = "1024m"
//        reports {
//            html.enabled = false
//        }
//        dependsOn preIntegrationTest
//        finalizedBy postIntegrationTest
//        /**
//         * Sets the JDBC URL
//         */
//        doFirst {
//            println "Setting JDBC URL for IT: ${rootProject.ext.itJdbcUrl}"
//            systemProperty "it.jdbc.url", rootProject.ext.itJdbcUrl
//            systemProperty "it.jdbc.user", rootProject.ext.itJdbcUsername
//            systemProperty "it.jdbc.password", rootProject.ext.itJdbcPassword
//        }
//    }
//
//    // Acceptance tests
//    task acceptanceTest(type: Test, dependsOn: "integrationTest") {
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
 * Acceptance tasks
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
//
///**
// * Development tasks
// */
//
//dockerCompose {
//    devInit {
//        useComposeFiles = ["compose/docker-compose-dev.yml"]
//    }
//}
//
//task devStart {
//    dependsOn devInitComposeUp
//}
//
//task devStop {
//    dependsOn devInitComposeDown
//}

/**
 * Publication tasks
 *
 * Standalone Gradle tasks in `gradle/publication.gradle` and in
 * `gradle/production.gradle`
 */

/**
 * General test report
 */

//task testReport(type: TestReport) {
//    destinationDir = file("$buildDir/reports/allTests")
//    // Include the results from the `test` tasks in all core subprojects
//    reportOn coreProjects*.test, coreProjects*.integrationTest, coreProjects*.acceptanceTest
//}
