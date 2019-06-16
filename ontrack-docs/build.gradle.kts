import org.asciidoctor.gradle.jvm.AsciidoctorPdfTask
import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    groovy
    id("org.asciidoctor.jvm.convert") version "2.2.0"
    id("org.asciidoctor.jvm.pdf") version "2.2.0"
}

apply<SpringBootPlugin>()

dependencies {
    implementation(project(":ontrack-dsl"))
    implementation(project(":ontrack-json"))
    implementation("commons-io:commons-io")
    implementation("org.apache.commons:commons-lang3")
}

if (project.hasProperty("documentation")) {

    val springBootVersion: String by project

    val generateDoc by tasks.registering(JavaExec::class) {
        dependsOn("classes")
        dependsOn(":ontrack-dsl:classes")
        main = "net.nemerosa.ontrack.docs.DSLDocGenerator"
        classpath = sourceSets["main"].runtimeClasspath
        args = listOf(
                project(":ontrack-dsl").file("src/main/groovy").absolutePath,
                "build/dsl"
        )
        inputs.dir(project(":ontrack-dsl").file("src/main/groovy"))
        outputs.dir("build/dsl")
    }

    val prepareGeneratedDoc by tasks.registering(Copy::class) {
        dependsOn(generateDoc)
        from("build/dsl")
        include("*.adoc")
        into("src/docs/asciidoc/generated")
    }

    // HTML specific settings

    tasks.named<AsciidoctorTask>("asciidoctor") {
        dependsOn(prepareGeneratedDoc)
        description = "Generates HTML documentation."
        attributes = mapOf(
                "ontrack-version" to version,
                "spring-boot-version" to springBootVersion,
                "icons" to "font"
        )
        logDocuments = true
        baseDirFollowsSourceDir()
        sources(delegateClosureOf<PatternSet> {
            include("index.adoc")
        })
        // FIXME requires("asciidoctor-diagram")
        sources("**/*.adoc")
    }

    // PDF specific settings

    tasks.named<AsciidoctorPdfTask>("asciidoctorPdf") {
        dependsOn("asciidoctor")
        description = "Generates PDF documentation."
        attributes = mapOf(
                "ontrack-version" to version,
                "spring-boot-version" to springBootVersion,
                "icons" to "font",
                "imagesdir" to file("build/asciidoc/html5")
        )
        logDocuments = true
        baseDirFollowsSourceDir()
        sources(delegateClosureOf<PatternSet> {
            include("index.adoc")
        })
        // FIXME requires("asciidoctor-diagram")
        sources("**/*.adoc")
    }

//    tasks.named("build") {
//        dependsOn("generateHtml")
//        dependsOn("generatePdf")
//    }

    rootProject.tasks.named<Zip>("publicationPackage") {
        //        dependsOn(generateHtml)
//        dependsOn(generatePdf)
//        from("${generateHtml.outputDir}/html5") {
//            into "html5"
//        }
//        from("${generatePdf.outputDir}/pdf") {
//            include "*.pdf"
//            into "pdf"
//        }
    }
}
