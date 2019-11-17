package net.nemerosa.ontrack.kdsl.test

import net.nemerosa.ontrack.kdsl.model.*
import net.nemerosa.ontrack.test.support.uid
import org.junit.Test
import kotlin.test.assertTrue

class KdslTestProjectLabels : AbstractKdslTest() {

    @Test
    fun `Creating and assigning project labels`() {
        // Given a project
        val projectName = uid("P")
        val project = ontrack.project(projectName)
        // Creates a global label
        val labelName = uid("L")
        ontrack.labels.createLabel("language", labelName)
        // Checks we can find the global label back
        assertTrue(ontrack.labels.list.any { it.category == "language" && it.name == labelName })
        // Assign to project
        project.assignLabel("language", labelName)
        // Checks it has been assigned
        assertTrue(project.labels.any { it.category == "language" && it.name == labelName })
    }

}