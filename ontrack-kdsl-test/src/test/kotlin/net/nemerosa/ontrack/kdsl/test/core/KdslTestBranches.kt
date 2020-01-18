package net.nemerosa.ontrack.kdsl.test.core

//import net.nemerosa.ontrack.kdsl.model.deprecated.branch
//import net.nemerosa.ontrack.kdsl.model.deprecated.branches
//import net.nemerosa.ontrack.kdsl.test.app.SpringTest
//import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
//import org.junit.Test
//import kotlin.test.assertEquals
//
//@SpringTest
//class KdslTestBranches : AbstractKdslTest() {
//
//    @Test
//    fun `Project branches`() {
//        project {
//            (1..5).forEach {
//                branch("B$it")
//            }
//            // Checks the branches
//            assertEquals(
//                    (5 downTo 1).map { "B$it" },
//                    branches().map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Project branches filtered by name`() {
//        project {
//            (1..5).forEach {
//                branch("B$it")
//            }
//            // Filter by regex
//            val branches = branches(name = "B(1|2)")
//            assertEquals(
//                    (2 downTo 1).map { "B$it" },
//                    branches.map { it.name }
//            )
//        }
//    }
//
//}