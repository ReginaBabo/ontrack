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

//if (documentationProfile) {
//    gradle.projectsEvaluated {
//        publicationPackage {
//            from javadocPackage
//        }
//    }
//}
//
//// Ontrack descriptor
//
//task deliveryDescriptor {
//    ext.output = project.file('build/ontrack.properties')
//    doLast {
//        (output as File).parentFile.mkdirs()
//        output.text = "# Ontrack properties\n"
//        // Version
//        output << "# Version information"
//        output << "VERSION_BUILD = ${project.versioning.info.build}\n"
//        output << "VERSION_BRANCH = ${project.versioning.info.branch}\n"
//        output << "VERSION_BASE = ${project.versioning.info.base}\n"
//        output << "VERSION_BRANCHID = ${project.versioning.info.branchId}\n"
//        output << "VERSION_BRANCHTYPE = ${project.versioning.info.branchType}\n"
//        output << "VERSION_COMMIT = ${project.versioning.info.commit}\n"
//        output << "VERSION_DISPLAY = ${project.versioning.info.display}\n"
//        output << "VERSION_FULL = ${project.versioning.info.full}\n"
//        output << "VERSION_SCM = ${project.versioning.info.scm}\n"
//        // Modules
//        output << "# Comma-separated list of modules\n"
//        output << "MODULES = ${project.subprojects.findAll { it.tasks.findByName('jar') }.collect { it.name }.join(',')}\n"
//    }
//}
//
//// Delivery package
//
//task deliveryPackage(type: Zip) {
//    classifier = 'delivery'
//    // Gradle files
//    from(projectDir) {
//        include 'buildSrc/**'
//        include '*.gradle'
//        include 'gradlew*'
//        include 'gradle/**'
//        include 'gradle.properties'
//        exclude '**/.gradle/**'
//        exclude '**/build/**'
//    }
//    // Acceptance
//    dependsOn ':ontrack-acceptance:normaliseJar'
//    from(project(':ontrack-acceptance').file('src/main/compose')) {
//      into 'ontrack-acceptance'
//    }
//    // All artifacts
//    dependsOn publicationPackage
//    from publicationPackage
//    // Descriptor
//    dependsOn deliveryDescriptor
//    from deliveryDescriptor.output
//}
//
//build.dependsOn deliveryPackage
