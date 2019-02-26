package net.nemerosa.ontrack.model.structure

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProjectBuildSearchContributorUtilsTest {

    private val project: Project = Project.of(NameDescription.nd("P", ""))

    @Test
    fun `Highest priority form`() {
        val form = ProjectBuildSearchContributorUtils.getSearchForm(
                project,
                "name",
                listOf(
                        TestProjectBuildSearchContributor(1, "One"),
                        TestProjectBuildSearchContributor(2, "Two")
                )
        )
        assertEquals("Two", form.promotionName)
    }

    @Test
    fun `Non null form`() {
        val form = ProjectBuildSearchContributorUtils.getSearchForm(
                project,
                "name",
                listOf(
                        TestProjectBuildSearchContributor(1, "One"),
                        TestProjectBuildSearchContributor(2, "Two", false)
                )
        )
        assertEquals("One", form.promotionName)
    }

    @Test
    fun `Default form`() {
        val form = ProjectBuildSearchContributorUtils.getSearchForm(
                project,
                "name",
                listOf(
                        TestProjectBuildSearchContributor(1, "One", false),
                        TestProjectBuildSearchContributor(2, "Two", false)
                )
        )
        assertNull(form.promotionName)
    }

    private class TestProjectBuildSearchContributor(
            override val priority: Int,
            private val promotion: String,
            private val present: Boolean = true
    ) : ProjectBuildSearchContributor {
        override fun getSearchForm(project: Project, name: String): BuildSearchForm? {
            return if (present) {
                BuildSearchForm().withBuildName(name).withPromotionName(promotion)
            } else {
                null
            }
        }
    }

}