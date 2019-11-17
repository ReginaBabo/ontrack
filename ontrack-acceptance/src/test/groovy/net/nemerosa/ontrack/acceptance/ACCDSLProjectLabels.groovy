package net.nemerosa.ontrack.acceptance

import net.nemerosa.ontrack.acceptance.support.AcceptanceTestSuite
import net.nemerosa.ontrack.acceptance.support.KDSL
import org.junit.Test

import static net.nemerosa.ontrack.test.TestUtils.uid
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * Acceptance tests for the project labels
 */
@AcceptanceTestSuite
class ACCDSLProjectLabels extends AbstractACCDSL {

    @Test
    @KDSL("KdslTestProjectLabels")
    void 'Creating and assigning project labels'() {
        def projectName = uid('P')
        def project = ontrack.project(projectName)

        // Creating a global label
        def name = uid('L')
        ontrack.config.label("language", name)

        assertTrue(
                ontrack.config.labels.find { it.category == "language" && it.name == name } != null
        )

        // Assign to project
        project.assignLabel("language", name)

        assertTrue(
                project.labels.find { it.category == "language" && it.name == name } != null
        )
    }

    @Test
    @KDSL("KdslTestProjectLabels")
    void 'Idempotent creation of labels'() {
        // Creating a global label
        def name = uid('L')
        ontrack.config.label("language", name)

        assertTrue(
                ontrack.config.labels.find { it.category == "language" && it.name == name } != null
        )

        // Creating it again
        ontrack.config.label("language", name)

        assertTrue(
                ontrack.config.labels.find { it.category == "language" && it.name == name } != null
        )
    }

    @Test
    @KDSL("KdslTestProjectLabels")
    void 'Assigning and unassigning project labels'() {
        def projectName = uid('P')
        def project = ontrack.project(projectName)

        // Creating a global label
        def name = uid('L')
        ontrack.config.label("language", name)

        // Assign to project
        project.assignLabel("language", name)

        assertTrue(
                project.labels.find { it.category == "language" && it.name == name } != null
        )

        // Unassigning from project
        project.unassignLabel("language", name)

        assertFalse(
                project.labels.find { it.category == "language" && it.name == name } != null
        )
    }

    @Test
    @KDSL("KdslTestProjectLabels")
    void 'Creating and assigning project label in same operation'() {
        def projectName = uid('P')
        def project = ontrack.project(projectName)

        // Creating a global label
        def name = uid('L')

        // Assign a new label to project, creating it at the same time
        project.assignLabel("language", name, true)

        assertTrue(
                ontrack.config.labels.find { it.category == "language" && it.name == name } != null
        )
        assertTrue(
                project.labels.find { it.category == "language" && it.name == name } != null
        )
    }

}
