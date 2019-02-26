package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.model.structure.ProjectBuildSearchContributor
import net.nemerosa.ontrack.model.structure.ProjectBuildSearchContributorUtils
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BuildLinkDisplayPropertyProjectBuildSearchContributorIT : AbstractPropertyTypeIT() {

    @Autowired
    private lateinit var contributor: BuildLinkDisplayPropertyProjectBuildSearchContributor

    @Autowired
    private lateinit var contributors: List<ProjectBuildSearchContributor>

    @Test
    fun `No form if no build link display property`() {
        project {
            val form = contributor.getSearchForm(this, "name")
            assertNull(form, "No form must be returned")
        }
    }

    @Test
    fun `No form if build link display property is disabled`() {
        project {
            buildLinkDisplay(useLabel = false)
            val form = contributor.getSearchForm(this, "name")
            assertNull(form, "No form must be returned")
        }
    }

    @Test
    fun `Form if build link display property is enabled`() {
        project {
            buildLinkDisplay(useLabel = true)
            val form = contributor.getSearchForm(this, "name")
            assertNotNull(form, "A form must be returned") {
                assertNull(it.buildName)
                assertEquals(false, it.isBuildExactMatch)
                assertEquals("net.nemerosa.ontrack.extension.general.ReleasePropertyType", it.property)
                assertEquals("name", it.propertyValue)
            }
        }
    }

    @Test
    fun `Default form if no build link display property`() {
        project {
            val form = ProjectBuildSearchContributorUtils.getSearchForm(this, "name", contributors)
            assertEquals("name", form.buildName)
            assertEquals(true, form.isBuildExactMatch)
        }
    }

    @Test
    fun `Default form if build link display property is disabled`() {
        project {
            buildLinkDisplay(useLabel = false)
            val form = ProjectBuildSearchContributorUtils.getSearchForm(this, "name", contributors)
            assertEquals("name", form.buildName)
            assertEquals(true, form.isBuildExactMatch)
        }
    }

    @Test
    fun `Custom form if build link display property is enabled`() {
        project {
            buildLinkDisplay(useLabel = true)
            val form = ProjectBuildSearchContributorUtils.getSearchForm(this, "name", contributors)
            assertNull(form.buildName)
            assertEquals(false, form.isBuildExactMatch)
            assertEquals("net.nemerosa.ontrack.extension.general.ReleasePropertyType", form.property)
            assertEquals("name", form.propertyValue)
        }
    }

}