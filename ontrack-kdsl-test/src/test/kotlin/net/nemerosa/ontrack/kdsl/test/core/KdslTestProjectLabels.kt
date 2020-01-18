package net.nemerosa.ontrack.kdsl.test.core

//import net.nemerosa.ontrack.kdsl.model.deprecated.*
//import net.nemerosa.ontrack.kdsl.test.app.SpringTest
//import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
//import net.nemerosa.ontrack.test.support.uid
//import org.junit.Test
//import kotlin.test.assertFalse
//import kotlin.test.assertNotNull
//import kotlin.test.assertTrue
//
//@SpringTest
//class KdslTestProjectLabels : AbstractKdslTest() {
//
//    @Test
//    fun `Creating and assigning project labels`() {
//        // Given a project
//        val projectName = uid("P")
//        val project = ontrack.project(projectName)
//        // Creates a global label
//        val labelName = uid("L")
//        ontrack.labels.createLabel("language", labelName)
//        // Checks we can find the global label back
//        assertTrue(ontrack.labels.list.any { it.category == "language" && it.name == labelName })
//        // Assign to project
//        project.assignLabel("language", labelName)
//        // Checks it has been assigned
//        assertTrue(project.labels.any { it.category == "language" && it.name == labelName })
//    }
//
//    @Test
//    fun `Idempotent creation of labels`() {
//        // Creating a global label
//        val name = uid("L")
//        ontrack.labels.label("language", name)
//        assertTrue(
//                ontrack.labels.list.find { it.category == "language" && it.name == name } != null
//        )
//
//        // Creating it again
//        ontrack.labels.label("language", name)
//
//        assertTrue(
//                ontrack.labels.list.find { it.category == "language" && it.name == name } != null
//        )
//    }
//
//    @Test
//    fun `Assigning and unassigning project labels`() {
//        val projectName = uid("P")
//        val project = ontrack.project(projectName)
//
//        // Creating a global label
//        val name = uid("L")
//        ontrack.labels.label("language", name)
//
//        // Assign to project
//        project.assignLabel("language", name)
//
//        assertTrue(
//                project.labels.find { it.category == "language" && it.name == name } != null
//        )
//
//        // Unassigning from project
//        project.unassignLabel("language", name)
//
//        assertFalse(
//                project.labels.find { it.category == "language" && it.name == name } != null
//        )
//    }
//
//    @Test
//    fun `Creating and assigning project label in same operation`() {
//        val projectName = uid("P")
//        val project = ontrack.project(projectName)
//
//        // Creating a global label
//        val name = uid("L")
//
//        // Assign a new label to project, creating it at the same time
//        project.assignLabel("language", name, true)
//
//        assertNotNull(ontrack.labels.findLabel("language", name))
//        assertTrue(
//                project.labels.find { it.category == "language" && it.name == name } != null
//        )
//    }
//
//}