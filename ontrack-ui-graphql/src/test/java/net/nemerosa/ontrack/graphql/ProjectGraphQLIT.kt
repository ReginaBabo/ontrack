package net.nemerosa.ontrack.graphql

import net.nemerosa.ontrack.model.structure.BranchFavouriteService
import net.nemerosa.ontrack.model.structure.NameDescription
import net.nemerosa.ontrack.model.structure.ProjectFavouriteService
import net.nemerosa.ontrack.model.structure.ValidationRunStatusID
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ProjectGraphQLIT : AbstractGraphQLITSupport() {

    @Autowired
    private lateinit var branchFavouriteService: BranchFavouriteService

    @Autowired
    private lateinit var projectFavouriteService: ProjectFavouriteService

    @Test
    fun `All projects`() {
        project {
            val data = runWithView("{projects { id name }}")
            assertNotNull(data["projects"].find { it["name"].asText() == name }) {
                assertEquals(id(), it["id"].asInt())
            }
        }
    }

    @Test
    fun `Project by ID`() {
        project {
            val data = runWithView(
                    """{
                    projects(id: $id) {
                        id
                        name
                    }
                }"""
            )
            val project = data["projects"][0]
            assertEquals(id(), project["id"].asInt())
            assertEquals(name, project["name"].asText())
        }
    }

    @Test
    fun `Project by name`() {
        project {
            val data = runWithView("""{projects(name:"$name") {id}}""")
            val project = data["projects"][0]
            assertEquals(id(), project["id"].asInt())
        }
    }

    @Test
    fun `Project by name when not authorized must throw an authentication exception`() {
        // Creates a project
        project {
            // Looks for this project by name, with a not authorized user
            assertFailsWith(AccessDeniedException::class, "Access denied") {
                withNoGrantViewToAll {
                    asUser().call {
                        run("""{ projects(name: "$name") { id }}""")
                    }
                }
            }
        }
    }

    @Test
    fun `Favourite projects`() {
        val account = doCreateAccount()
        withGrantViewToAll {
            val project = project()
            val favProject = project {
                asAccount(account).execute {
                    projectFavouriteService.setProjectFavourite(this, true)
                }
            }
            // Gets the favourite projects
            val data = asAccount(account).call {
                run(""" { projects(favourites: true) { id } } """)
            }
            // Checks the favourites projects are there
            assertNotNull(data["projects"].find { it["id"].asInt() == favProject.id() })
            // ... and not the other one
            assertNull(data["projects"].find { it["id"].asInt() == project.id() })
        }
    }

}