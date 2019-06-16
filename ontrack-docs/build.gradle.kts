import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    groovy
    id("org.asciidoctor.convert") version "2.2.0"
}

apply<SpringBootPlugin>()

dependencies {
    asciidoctor("org.asciidoctor:asciidoctorj-pdf:1.5.0-alpha.14")
    implementation(project(":ontrack-dsl"))
    implementation(project(":ontrack-json"))
    implementation("commons-io:commons-io")
    implementation("org.apache.commons:commons-lang3")
}

if (project.hasProperty("documentation")) {

    asciidoctorj {
        version = "1.5.6"
    }

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

//    val generateHtml by tasks.registering(AsciidoctorTask::class) {
//        description = "Generates HTML documentation."
//        group =  "Documentation"
//        backends("html5")
//        attributes = mapOf(
//                "ontrack-version"    to version,
//        "spring-boot-version" to springBootVersion,
//        "icons"              to "font"
//        )
//    }
//
//    // PDF specific settings
//
//    val generatePdf by tasks.registering(AsciidoctorTask::class) {
//        description = "Generates PDF documentation."
//        group =  "Documentation"
//        mustRunAfter(generateHtml)
//        backends("pdf")
//        attributes = mapOf(
//                "ontrack-version"    to version,
//                "spring-boot-version" to springBootVersion,
//                "icons"              to "font",
//                "imagesdir"          to file("build/asciidoc/html5")
//        )
//    }

    // common Asciidoctor settings

    tasks.withType(AsciidoctorTask::class) {
        dependsOn(prepareGeneratedDoc)
        // FIXME requires("asciidoctor-diagram")
        sources(delegateClosureOf<PatternSet> {
            include("index.adoc")
        })
        logDocuments = true
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
