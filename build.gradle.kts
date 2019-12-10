import com.avast.gradle.dockercompose.ComposeExtension
import com.avast.gradle.dockercompose.tasks.ComposeUp
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.netflix.gradle.plugins.deb.Deb
import com.netflix.gradle.plugins.packaging.SystemPackagingTask
import com.netflix.gradle.plugins.rpm.Rpm
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import net.nemerosa.ontrack.gradle.OntrackLastReleases
import net.nemerosa.ontrack.gradle.RemoteAcceptanceTest
import net.nemerosa.versioning.VersioningExtension
import net.nemerosa.versioning.VersioningPlugin
import org.ajoberstar.gradle.git.publish.GitPublishExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.redline_rpm.header.Os
import org.springframework.boot.gradle.plugin.SpringBootPlugin

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        val kotlinVersion: String by project
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
    }
}

// FIXME Try to use version in gradle.properties
// val springBootVersion: String by project
val kotlinVersion: String by project

plugins {
    java
    jacoco
    id("net.nemerosa.versioning") version "2.8.2" apply false
    id("nebula.deb") version "6.2.1"
    id("nebula.rpm") version "6.2.1"
    id("org.sonarqube") version "2.5"
    id("com.avast.gradle.docker-compose") version "0.9.5"
    id("com.bmuschko.docker-remote-api") version "4.1.0"
    id("org.springframework.boot") version "2.2.2.RELEASE" apply false
    id("io.freefair.aggregate-javadoc") version "4.1.2"
    // Site
    id("org.ajoberstar.git-publish") version "2.1.1"
}

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

// Pre-integration tests: starting Postgresql

configure<ComposeExtension> {
    createNested("integrationTest").apply {
        useComposeFiles = listOf("compose/docker-compose-it.yml")
        projectName = itProject
    }
}

val preIntegrationTest by tasks.registering {
    dependsOn("integrationTestComposeUp")
    // When done
    doLast {
        val host = tasks.named<ComposeUp>("integrationTestComposeUp").get().servicesInfos["db"]?.host!!
        val port = tasks.named<ComposeUp>("integrationTestComposeUp").get().servicesInfos["db"]?.firstContainer?.tcpPort!!
        val url = "jdbc:postgresql://$host:$port/ontrack"
        val jdbcUrl: String by rootProject.extra(url)
        logger.info("Pre integration test JDBC URL = ${jdbcUrl}")
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

val coreProjects = javaProjects.filter {
    it.path != ":ontrack-dsl"
}

configure(javaProjects) p@{

    /**
     * For all Java projects
     */

    apply(plugin = "java")
    apply(plugin = "maven-publish")

    // Documentation

    if (hasProperty("documentation")) {

        // Javadoc

        val javadocJar = tasks.register<Jar>("javadocJar") {
            archiveClassifier.set("javadoc")
            from("javadoc")
        }

        // Sources

        val sourcesJar = tasks.register<Jar>("sourcesJar") {
            dependsOn(JavaPlugin.CLASSES_TASK_NAME)
            archiveClassifier.set("sources")
            from(project.the<SourceSetContainer>()["main"].allSource)
        }

        artifacts {
            add("archives", javadocJar)
            add("archives", sourcesJar)
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
                            email.set("damien.coraboeuf@nemerosa.com")
                        }
                    }
                }
            }
        }
    }

    tasks.named("assemble") {
        dependsOn("generatePomFileForMavenCustomPublication")
    }

}

configure(coreProjects) p@{

    /**
     * For all Java projects
     */

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "io.spring.dependency-management")

    configure<DependencyManagementExtension> {
        imports {
            mavenBom(SpringBootPlugin.BOM_COORDINATES) {
                bomProperty("kotlin.version", kotlinVersion)
            }
        }
        dependencies {
            dependency("commons-io:commons-io:2.6")
            dependency("org.apache.commons:commons-text:1.8")
            dependency("net.jodah:failsafe:1.1.1")
            dependency("commons-logging:commons-logging:1.2")
            dependency("org.apache.commons:commons-math3:3.6.1")
            dependency("args4j:args4j:2.33")
            dependency("org.jgrapht:jgrapht-core:1.3.0")
            dependency("org.kohsuke:groovy-sandbox:1.19")
            dependency("com.graphql-java:graphql-java:11.0")
            dependency("org.jetbrains.kotlin:kotlin-test:${kotlinVersion}")
            // Overrides from Spring Boot
            dependency("org.postgresql:postgresql:9.4.1208")
        }
    }

    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
        "implementation"("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
        // Lombok
        "compileOnly"("org.projectlombok:lombok:1.18.10")
        "annotationProcessor"("org.projectlombok:lombok:1.18.10")
        "testCompileOnly"("org.projectlombok:lombok:1.18.10")
        "testAnnotationProcessor"("org.projectlombok:lombok:1.18.10")
        // Testing
        "testImplementation"("junit:junit")
        "testImplementation"("org.mockito:mockito-core")
        "testImplementation"("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    // Unit tests run with the `test` task
    tasks.named<Test>("test") {
        include("**/*Test.class")
    }

    // Integration tests
    val integrationTest by tasks.registering(Test::class) {
        mustRunAfter("test")
        include("**/*IT.class")
        minHeapSize = "512m"
        maxHeapSize = "1024m"
        dependsOn(":preIntegrationTest")
        finalizedBy(":postIntegrationTest")
        /**
         * Sets the JDBC URL
         */
        doFirst {
            println("Setting JDBC URL for IT: ${rootProject.ext["jdbcUrl"]}")
            systemProperty("spring.datasource.url", rootProject.ext["jdbcUrl"]!!)
            systemProperty("spring.datasource.username", "ontrack")
            systemProperty("spring.datasource.password", "ontrack")
        }
    }

    // Synchronization with shutting down the database
    rootProject.tasks.named("integrationTestComposeDown") {
        mustRunAfter(integrationTest)
    }

}



/**
 * Code coverage report
 */

configure(coreProjects) {
    apply(plugin = "jacoco")
}

val codeCoverageReport by tasks.registering(JacocoReport::class) {
    executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))

    javaProjects.forEach {
        sourceSets(it.sourceSets["main"])
    }

    reports {
        xml.isEnabled = true
        xml.destination = file("${buildDir}/reports/jacoco/report.xml")
        html.isEnabled = false
        csv.isEnabled = false
    }
}

configure(javaProjects) {
    tasks.named("test") test@{
        codeCoverageReport {
            dependsOn(this@test)
        }
    }
    val integrationTest = tasks.findByName("integrationTest")
    if (integrationTest != null) {
        codeCoverageReport {
            dependsOn(integrationTest)
        }
    }
}

val jacocoExecFile: String by project
val jacocoReportFile: String by project

val codeDockerCoverageReport by tasks.registering(JacocoReport::class) {
    executionData(fileTree(project.rootDir.absolutePath).include(jacocoExecFile))

    javaProjects.forEach {
        sourceSets(it.sourceSets["main"])
    }

    reports {
        xml.isEnabled = true
        xml.destination = file(jacocoReportFile)
        html.isEnabled = false
        csv.isEnabled = false
    }
}

/**
 * Packaging of Ontrack for the subsequent stages of the validation.
 *
 * This creates a ZIP which contains:
 *
 * - all Gradle files needed for the execution of the pipeline
 * - the `buildSrc` which contains the Gradle helper classes
 * - the UI JAR artifact
 * - the Acceptance JAR artifact
 * - a ZIP containing ALL artifacts (POM files, JAR, Javadoc & Sources)
 * - an `ontrack.properties` file which contains the list of modules & the version information
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

// Global Javadoc

tasks.named<Javadoc>("aggregateJavadoc") {
    include("net/nemerosa/**")
}

if (project.hasProperty("documentation")) {

    rootProject.tasks.register("javadocPackage", Zip::class) {
        archiveClassifier.set("javadoc")
        archiveFileName.set("ontrack-javadoc.zip")
        dependsOn("aggregateJavadoc")
        from(rootProject.file("$rootProject.buildDir/docs/javadoc"))
    }
}

// ZIP package which contains all artifacts to be published

val publicationPackage by tasks.registering(Zip::class) {
    archiveClassifier.set("publication")
    archiveFileName.set("ontrack-publication.zip")
    // Extension test module
    from(rootProject.file("ontrack-extension-test")) {
        into("ontrack-extension-test")
    }
}

subprojects {
    afterEvaluate p@{
        val jar = tasks.findByName("jar") as? Jar?
        if (jar != null && jar.isEnabled) {
            publicationPackage {
                from(jar)
            }
            if (rootProject.hasProperty("documentation")) {
                val javadoc = tasks.findByName("javadocJar")
                if (javadoc != null) {
                    publicationPackage {
                        from(javadoc)
                    }
                }
                val sources = tasks.findByName("sourcesJar")
                if (sources != null) {
                    publicationPackage {
                        from(sources)
                    }
                }
            }
            val testJar = tasks.findByName("testJar")
            if (testJar != null) {
                publicationPackage {
                    from(testJar)
                }
            }
            // POM file
            val pomTask = tasks.findByName("generatePomFileForMavenCustomPublication")
            if (pomTask != null) {
                publicationPackage {
                    from(pomTask) {
                        rename { "${this@p.name}-${this@p.version}.pom" }
                    }
                }
            }
        }
    }
}

if (hasProperty("documentation")) {
    gradle.projectsEvaluated {
        val javadocPackage = tasks.named("javadocPackage")
        publicationPackage {
            from(javadocPackage)
        }
    }
}

// Delivery package

val deliveryPackage by tasks.registering(Zip::class) {
    archiveClassifier.set("delivery")
    // Gradle files
    from(projectDir) {
        include("buildSrc/**")
        include("*.gradle")
        include("gradlew*")
        include("gradle/**")
        include("gradle.properties")
        exclude("**/.gradle/**")
        exclude("**/build/**")
    }
    // Acceptance
    dependsOn(":ontrack-acceptance:normaliseJar")
    from(project(":ontrack-acceptance").file("src/main/compose")) {
        into("ontrack-acceptance")
    }
    // All artifacts
    dependsOn(publicationPackage)
    from(publicationPackage)
    // Descriptor (defined in main build.gradle)
    dependsOn("deliveryDescriptor")
    from(tasks.getByName("deliveryDescriptor").extra["output"])
}

tasks.named("build") {
    dependsOn(deliveryPackage)
}

/**
 * Packaging for OS
 *
 * The package version does not accept versions like the ones generated
 * from the Versioning plugin for the feature branches for example.
 */

val packageVersion: String = if (version.toString().matches("\\d+\\.\\d+\\.\\d+".toRegex())) {
    version.toString().replace("[^0-9\\.-_]".toRegex(), "")
} else {
    "0.0.0"
}
println("Using package version = $packageVersion")

val debPackage by tasks.registering(Deb::class) {
    dependsOn(":ontrack-ui:bootJar")

    link("/etc/init.d/ontrack", "/opt/ontrack/bin/ontrack.sh")
}

val rpmPackage by tasks.registering(Rpm::class) {
    dependsOn(":ontrack-ui:bootJar")

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
    dependsOn(":ontrack-ui:bootJar")
    from("ontrack-ui/build/libs")
    include("*.jar")
    exclude("*-javadoc.jar")
    exclude("*-sources.jar")
    into(project.file("docker"))
    rename(".*", "ontrack.jar")
}

val dockerBuild by tasks.registering(DockerBuildImage::class) {
    dependsOn(dockerPrepareEnv)
    inputDir.set(file("docker"))
    tags.add("nemerosa/ontrack:$version")
    tags.add("nemerosa/ontrack:latest")
}

/**
 * Acceptance tasks
 */

dockerCompose {
    createNested("local").apply {
        useComposeFiles = listOf("compose/docker-compose-local.yml")
        projectName = "ci"
        captureContainersOutputToFiles = project.file("${project.buildDir}/local-logs")
        tcpPortsToIgnoreWhenWaiting = listOf(8083, 8086)
    }
}

tasks.named<ComposeUp>("localComposeUp") {
    dependsOn(dockerBuild)
    doLast {
        val host = servicesInfos["ontrack"]?.host!!
        val port = servicesInfos["ontrack"]?.firstContainer?.tcpPort!!
        val url = "http://$host:$port"
        val ontrackUrl: String by rootProject.extra(url)
        logger.info("Pre acceptance test Ontrack URL = $ontrackUrl")
    }
}

tasks.register("localAcceptanceTest", RemoteAcceptanceTest::class) {
    acceptanceUrlFn = {
        rootProject.extra["ontrackUrl"] as String
    }
    disableSsl = true
    acceptanceTimeout = 300
    acceptanceImplicitWait = 30
    dependsOn("localComposeUp")
    finalizedBy("localComposeDown")
}

/**
 * Development tasks
 */

val devPostgresName: String by project
val devPostgresPort: String by project

dockerCompose {
    createNested("dev").apply {
        useComposeFiles = listOf("compose/docker-compose-dev.yml")
        projectName = "dev"
        captureContainersOutputToFiles = project.file("${project.buildDir}/dev-logs")
        environment["POSTGRES_NAME"] = devPostgresName
        environment["POSTGRES_PORT"] = devPostgresPort
    }
}

val devStart by tasks.registering {
    dependsOn("devComposeUp")
}

val devStop by tasks.registering {
    dependsOn("devComposeDown")
}

/**
 * Site generation
 *
 * Must be called AFTER the current version has been promoted in Ontrack to the RELEASE promotion level.
 *
 * This means having a Site job in the pipeline, after the Publish one, calling the `site` task.
 */

val ontrackVersion: String by project

val siteOntrackLast2Releases by tasks.registering(OntrackLastReleases::class) {
    releaseCount = 2
    releasePattern = "2\\.[\\d]+\\..*"
}

val siteOntrackLast3Releases by tasks.registering(OntrackLastReleases::class) {
    releaseCount = 6
    releasePattern = "3\\.[\\d]+\\..*"
}

val sitePagesDocJs by tasks.registering {
    dependsOn(siteOntrackLast2Releases)
    dependsOn(siteOntrackLast3Releases)
    outputs.file(project.file("build/site/page/doc.js"))
    doLast {
        val allReleases = siteOntrackLast3Releases.get().releases + siteOntrackLast2Releases.get().releases
        val allVersions = allReleases.joinToString(",") { "'${it.name}'" }
        project.file("build/site/page").mkdirs()
        project.file("build/site/page/doc.js").writeText(
                """var releases = [$allVersions];"""
        )
    }
}

configure<GitPublishExtension> {
    repoUri.set(project.properties["ontrackGitHubUri"] as String)
    branch.set(project.properties["ontrackGitHubPages"] as String)
    contents {
        from("ontrack-site/src/main/web")
        from("build/site/page") {
            include("doc.js")
            into("javascripts/doc/")
        }
    }
    commitMessage.set("GitHub pages for version $ontrackVersion")
}

tasks.named("gitPublishCopy") {
    dependsOn(sitePagesDocJs)
}

val site by tasks.registering {
    dependsOn("gitPublishPush")
}
