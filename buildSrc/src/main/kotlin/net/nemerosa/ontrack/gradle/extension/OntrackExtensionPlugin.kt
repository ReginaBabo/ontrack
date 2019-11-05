package net.nemerosa.ontrack.gradle.extension

import com.liferay.gradle.plugins.node.NodeExtension
import com.liferay.gradle.plugins.node.NodePlugin
import com.liferay.gradle.plugins.node.tasks.ExecuteNodeScriptTask
import com.liferay.gradle.plugins.node.tasks.NpmInstallTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*

class OntrackExtensionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.logger.info("[ontrack] Applying INTERNAL Ontrack plugin to ${target.path}")

        /**
         * Project"s configuration
         */

        val ontrack = target.extensions.create<OntrackExtension>("ontrack")

        /**
         * Node plugin setup
         */

        target.apply<NodePlugin>()
        target.configure<NodeExtension> {
            isGlobal = true
            setNodeVersion("8.10.0")
            setNpmVersion("5.7.1")
            isDownload = true
        }

        /**
         * Project configuration
         */

        target.extra["ontrackWebDir"] = target.file(".gradle/ontrack")
        target.extra["ontrackWebNodeModulesDir"] = target.file("node_modules")

        /**
         * NPM tasks
         */

        val copyPackageJson by target.tasks.registering {
            doLast {
                logger.info("[ontrack] Copies the package.json file to ${target.projectDir}")
                OntrackExtensionPlugin::class.java.getResourceAsStream("/extension/package.json").copyTo(
                        target.file("package.json").outputStream()
                )
            }
        }

        val copyGulpFile by target.tasks.registering {
            doLast {
                logger.info("[ontrack] Copies the gulpfile.js file to ${target.projectDir}")
                OntrackExtensionPlugin::class.java.getResourceAsStream("/extension/gulpfile.js").copyTo(
                        target.file("gulpfile.js").outputStream()
                )
            }
        }

        target.tasks.named<NpmInstallTask>("npmInstall") {
            dependsOn(copyPackageJson)
            setNodeModulesCacheDir("${target.rootDir}/.gradle/node_modules_cache")
            setWorkingDir(target.projectDir)
            inputs.file(target.file("package.json"))
            outputs.dir(target.file("node_modules"))
        }

        /**
         * Gulp call
         */

        val web by target.tasks.registering(ExecuteNodeScriptTask::class) {
            dependsOn(target.tasks.named("npmInstall"))
            dependsOn(copyGulpFile)

            if (target.file("src/main/resources/static").exists()) {
                inputs.dir(target.file("src/main/resources/static"))
            }
            outputs.file(target.file("build/web/dist/module.js"))

            doFirst {
                target.mkdir(target.buildDir)
                logger.info("[ontrack] Generating web resources of ${ontrack.id(project)} in ${target.buildDir}")
            }

            setWorkingDir(target.projectDir)
            setScriptFile(target.file("node_modules/gulp/bin/gulp"))
            args(
                    "default",
                    "--extension", ontrack.id(project),
                    "--version", target.version,
                    "--src", target.file("src/main/resources/static"),
                    "--target", target.buildDir
            )
        }

        /**
         * Version file
         */

        val ontrackProperties by target.tasks.registering {
            description = "Prepares the ontrack META-INF file"
            doLast {
                target.mkdir("build")
                target.file("build/ontrack.properties").writeText("""
                    # Ontrack extension properties
                    version = ${target.version}
                    """.trimIndent()
                )
            }
        }

        /**
         * Update of the JAR task
         */

        target.tasks.named<Jar>("jar") {
            dependsOn(web)
            dependsOn(ontrackProperties)
            from("build/web/dist") {
                into("static/extension/${ontrack.id(project)}/${target.version}/")
            }
            from("build") {
                include("ontrack.properties")
                into("META-INF/ontrack/extension/")
                rename { "${ontrack.id(project)}.properties" }
            }
            exclude("static/**/*.js")
            exclude("static/**/*.html")
        }

        /**
         * Kotlin DSL management
         */

        val dslDir = target.file("src/dsl")
        if (dslDir.exists() && dslDir.isDirectory) {
            val kotlinVersion: String by target
            val javaConvention = target.convention.getPlugin(JavaPluginConvention::class.java)
            val sourceSets = javaConvention.sourceSets
            sourceSets.create("dsl")

            val dslImplementation by target.configurations.getting

            target.dependencies {
                dslImplementation(project(":ontrack-kdsl-model"))
                dslImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
                dslImplementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
            }

            val dslJar by target.tasks.registering(Jar::class) {
                archiveClassifier.set("dsl")
                from(sourceSets["dsl"].output)
            }

            target.configure<PublishingExtension> {
                publications {
                    maybeCreate<MavenPublication>("mavenCustom").artifact(target.tasks["dslJar"])
                }
            }

            target.tasks["assemble"].dependsOn(dslJar)

            val dslConfig by target.configurations.creating

            target.artifacts {
                add("dslConfig", dslJar)
            }

        }

        /**
         * BDD extensions management
         */

        val bddDir = target.file("src/bdd")
        if (bddDir.exists() && bddDir.isDirectory) {
            val kotlinVersion: String by target
            val javaConvention = target.convention.getPlugin(JavaPluginConvention::class.java)
            val sourceSets = javaConvention.sourceSets
            sourceSets.create("bdd")

            val bddImplementation by target.configurations.getting

            target.dependencies {
                bddImplementation(project(":ontrack-bdd-model"))
                bddImplementation(project(path = target.path, configuration = "dslConfig"))
                bddImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
                bddImplementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
            }

            val bddJar by target.tasks.registering(Jar::class) {
                archiveClassifier.set("bdd")
                from(sourceSets["bdd"].output)
            }

            target.configure<PublishingExtension> {
                publications {
                    maybeCreate<MavenPublication>("mavenCustom").artifact(target.tasks["bddJar"])
                }
            }

            target.tasks["assemble"].dependsOn(bddJar)

            val bddConfig by target.configurations.creating

            target.artifacts {
                add("bddConfig", bddJar)
            }

        }

    }

}
