package net.nemerosa.ontrack.gradle

import groovy.util.Node
import groovy.xml.QName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.GenerateMavenPom

/**
 * Compatibility plugin between Spring Boot 1.5.x and Gradle 5.x. Will have to be removed
 * when switching to Spring Boot 2.x
 */
class OntrackBoot15Plugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.withType(GenerateMavenPom::class.java) { task ->
            task.pom.withXml { xml ->
                xml.asNode().apply {
                    val name = QName("dependencyManagement")
                    val list = getAt(name)
                    val node = list[0] as Node
                    remove(node)
                }
            }

        }
    }
}