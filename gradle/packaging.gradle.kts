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

// Global Javadoc

if (hasProperty("documentation")) {

    apply(plugin = "nebula-aggregate-javadocs")

    gradle.projectsEvaluated {

        tasks.named<Javadoc>("aggregateJavadocs") {
            include("net/nemerosa/**")
        }

        tasks.register<Zip>("javadocPackage") {
            archiveClassifier.set("javadoc")
            archiveFileName.set("ontrack-javadoc.zip")
            dependsOn("aggregateJavadocs")
            from("build/docs/javadoc")
        }

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
    afterEvaluate {
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
//                // FIXME POM file
//                // from "${project.buildDir}/poms/${project.name}-${project.version}.pom"
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
