package net.nemerosa.ontrack.graphql

import net.nemerosa.ontrack.model.structure.Branch
import net.nemerosa.ontrack.model.structure.BranchFavouriteService
import net.nemerosa.ontrack.model.structure.NameDescription
import net.nemerosa.ontrack.test.TestUtils.uid
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

class BranchGraphQLIT : AbstractGraphQLITSupport() {

    @Autowired
    private lateinit var branchFavouriteService: BranchFavouriteService

    @Test
    fun `Branch by name`() {
        project {
            branch("B1")
            branch("B2")
            val data = run("""{projects(id: $id) { name branches(name: "B2") { name } } }""")
            val names = data["projects"][0]["branches"].map { it["name"].asText() }
            assertEquals(listOf("B2"), names)
        }
    }

    @Test
    fun `All favourite branches`() {
        val account = doCreateAccount()
        val branch1 = project<Branch> {
            branch {}
            branch {
                asAccount(account).withView(this).execute {
                    branchFavouriteService.setBranchFavourite(this, true)
                }
            }
        }
        val branch2 = project<Branch> {
            branch {}
            branch {
                asAccount(account).withView(this).execute {
                    branchFavouriteService.setBranchFavourite(this, true)
                }
            }
        }
        // Gets ALL the favourite branches
        val data = asAccount(account).withView(branch1).withView(branch2).call {
            run(""" { branches(favourite: true) { id } } """)
        }
        val branchIds: Set<Int> = data["branches"].map { it["id"].asInt() }.toSet()
        assertEquals(
                setOf(branch1.id(), branch2.id()),
                branchIds
        )
    }

    @Test
    fun `Favourite branch on one project`() {
        val account = doCreateAccount()
        project {
            val fav = branch {
                asAccount(account).withView(this).execute {
                    branchFavouriteService.setBranchFavourite(this, true)
                }
            }
            branch {}
            // Gets the favourite branches
            val data = asAccount(account).withView(this).call {
                run("""
                    {
                        branches(project: "${this.name}", favourite: true) {
                            id
                        }
                    }
                """)
            }
            val branchIds: Set<Int> = data["branches"].map { it["id"].asInt() }.toSet()
            assertEquals(
                    setOf(fav.id()),
                    branchIds
            )
        }
    }

    @Test
    fun `Branch by name on two different projects`() {
        val name = uid("B")

        val p1 = doCreateProject()
        val b1 = doCreateBranch(p1, NameDescription.nd(name, ""))
        doCreateBranch(p1, NameDescription.nd("B2", ""))
        val p2 = doCreateProject()
        val b2 = doCreateBranch(p2, NameDescription.nd(name, ""))

        val data = run("""{branches (name: "$name") { id } }""")
        assertEquals(
                setOf(b1.id(), b2.id()),
                data["branches"].map { it["id"].asInt() }.toSet()
        )
    }

    @Test
    fun `Favourite branches for project`() {
        val account = doCreateAccount()
        project {
            branch {}
            val fav = branch {
                asAccount(account).withView(this).execute {
                    branchFavouriteService.setBranchFavourite(this, true)
                }
            }
            // Gets the favourite branches in project
            val data = asAccount(account).withView(this).call {
                run("""
                    {
                        projects(id: ${this.id}) {
                            branches(favourite: true) {
                                id
                            }
                        }
                    }
                """)
            }
            val branchIds: Set<Int> = data["projects"][0]["branches"].map { it["id"].asInt() }.toSet()
            assertEquals(
                    setOf(fav.id()),
                    branchIds
            )
        }
    }

}