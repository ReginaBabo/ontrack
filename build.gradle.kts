import com.avast.gradle.dockercompose.ComposeExtension
import com.avast.gradle.dockercompose.tasks.ComposeUp
import java.lang.Thread.sleep

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.netflix.nebula:gradle-aggregate-javadocs-plugin:3.0.1")
        classpath("io.spring.gradle:dependency-management-plugin:1.0.8.RELEASE")
    }
}

/**
 * Build properties
 */

val micrometerVersion: String by project
val springBootVersion: String by project

/**
 * Plugins
 */

plugins {
    id("net.nemerosa.versioning") version "2.8.2"
    id("nebula.os-package") version "2.2.6"
    id("org.sonarqube") version "2.5"
    id("com.avast.gradle.docker-compose") version "0.9.4"
    // FIXME Spring Boot
}

/**
 * Meta information
 */

allprojects {
    group = "net.nemerosa.ontrack"
    version = versioning.info.display
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

val coreProjects = subprojects.filter {
    it.path != ":ontrack-dsl"
}

configure(javaProjects) p@{

    /**
     * For all Java projects
     */

    plugins {
        java
        `maven-publish`
    }

    // Javadoc

    if (hasProperty("documentation")) {

        tasks.register<Jar>("javadocJar") {
            classifier = "javadoc"
            from("javadoc")
        }

        // Sources

        tasks.register<Jar>("sourcesJar") {
            classifier = "sources"
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


configure(coreProjects) {

//
//    /**
//     * For all Java projects
//     */
//
//    apply plugin: "kotlin"
//    apply plugin: "kotlin-spring"
//    apply plugin: "io.spring.dependency-management"
//
//    dependencyManagement {
//        imports {
//            mavenBom("org.springframework.boot:spring-boot-dependencies:${springBootVersion}")
//        }
//        dependencies {
//            dependency "commons-io:commons-io:2.6"
//            dependency "org.apache.commons:commons-lang3:3.8.1"
//            dependency "org.apache.commons:commons-text:1.6"
//            dependency "org.projectlombok:lombok:1.16.18"
//            dependency "net.jodah:failsafe:1.1.1"
//            dependency "commons-logging:commons-logging:1.2"
//            dependency "org.apache.commons:commons-math3:3.6.1"
//            dependency "com.google.guava:guava:27.0.1-jre"
//            dependency "net.sf.dbinit:dbinit:1.4.0"
//            dependency "args4j:args4j:2.33"
//            dependency "org.jgrapht:jgrapht-core:1.3.0"
//            dependency "org.kohsuke:groovy-sandbox:1.19"
//            dependency "com.graphql-java:graphql-java:11.0"
//            dependency "org.jetbrains.kotlin:kotlin-test:${kotlinVersion}"
//            // Metrics
//            dependency "io.micrometer:micrometer-core:${micrometerVersion}"
//            dependency "io.micrometer:micrometer-spring-legacy:${micrometerVersion}"
//            dependency "io.micrometer:micrometer-registry-influx:${micrometerVersion}"
//            dependency "io.micrometer:micrometer-registry-prometheus:${micrometerVersion}"
//            // InfluxDB
//            dependency "org.influxdb:influxdb-java:2.14"
//            // Overrides from Spring Boot
//            dependency "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8"
//            dependency "org.postgresql:postgresql:9.4.1208"
//            dependency "org.flywaydb:flyway-core:4.2.0"
//        }
//    }
//
//    dependencies {
//        compile "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}"
//        compile "org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}"
//        compileOnly "org.projectlombok:lombok"
//        testCompile "org.projectlombok:lombok"
//        // Testing
//        testCompile "junit:junit"
//        testCompile "org.mockito:mockito-core"
//        testCompile "org.jetbrains.kotlin:kotlin-test"
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
}

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
