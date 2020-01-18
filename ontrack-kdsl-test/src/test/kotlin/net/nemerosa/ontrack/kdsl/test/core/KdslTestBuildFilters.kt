package net.nemerosa.ontrack.kdsl.test.core

//import net.nemerosa.ontrack.kdsl.model.deprecated.*
//import net.nemerosa.ontrack.kdsl.test.app.SpringTest
//import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
//import net.nemerosa.ontrack.kdsl.test.support.withTestBranch
//import org.junit.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertTrue
//
//@SpringTest
//class KdslTestBuildFilters : AbstractKdslTest() {
//
//    @Test
//    fun `Getting last promoted build`() {
//        withTestBranch { branch ->
//            val results = branch.lastPromotedBuilds
//            assertEquals(
//                    listOf("3", "2", "1"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filter interval`() {
//        withTestBranch { branch ->
//            val results = branch.intervalFilter(from = "3", to = "1")
//            assertEquals(
//                    listOf("3", "2", "1"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filter interval in reverse order`() {
//        withTestBranch { branch ->
//            val results = branch.intervalFilter(from = "1", to = "3")
//            assertEquals(
//                    listOf("3", "2", "1"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filter interval - only two`() {
//        withTestBranch { branch ->
//            val results = branch.intervalFilter(from = "2", to = "3")
//            assertEquals(
//                    listOf("3", "2"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filter interval - only one`() {
//        withTestBranch { branch ->
//            val results = branch.intervalFilter(from = "2", to = "2")
//            assertEquals(
//                    listOf("2"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filter interval - not existing`() {
//        withTestBranch {
//            val results = it.intervalFilter(from = "2", to = "4")
//            assertTrue(
//                    results.isEmpty(),
//                    "No build is returned"
//            )
//        }
//    }
//
//
//    @Test
//    fun `Filtering build on promotion`() {
//        withTestBranch { branch ->
//            val results = branch.standardFilter(
//                    withPromotionLevel = "BRONZE"
//            )
//            assertEquals(
//                    listOf("2"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filtering build - with validation (any)`() {
//        withTestBranch { branch ->
//            ontrack.build(branch.project.name, branch.name, "2").validate("SMOKE", "FAILED")
//            ontrack.build(branch.project.name, branch.name, "3").validate("SMOKE", "PASSED")
//            val results = branch.standardFilter(
//                    withValidationStamp = "SMOKE"
//            )
//            assertEquals(
//                    listOf("3", "2"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filtering build - with validation (passed)`() {
//        withTestBranch { branch ->
//            ontrack.build(branch.project.name, branch.name, "2").validate("SMOKE", "FAILED")
//            ontrack.build(branch.project.name, branch.name, "3").validate("SMOKE", "PASSED")
//            val results = branch.standardFilter(
//                    withValidationStamp = "SMOKE",
//                    withValidationStampStatus = "PASSED"
//            )
//            assertEquals(
//                    listOf("3"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filtering build - since validation (any)`() {
//        withTestBranch { branch ->
//            ontrack.build(branch.project.name, branch.name, "1").validate("SMOKE", "PASSED")
//            ontrack.build(branch.project.name, branch.name, "2").validate("SMOKE", "FAILED")
//            val results = branch.standardFilter(
//                    sinceValidationStamp = "SMOKE"
//            )
//            assertEquals(
//                    listOf("3", "2"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//    @Test
//    fun `Filtering build - since validation (passed)`() {
//        withTestBranch { branch ->
//            ontrack.build(branch.project.name, branch.name, "1").validate("SMOKE", "PASSED")
//            ontrack.build(branch.project.name, branch.name, "2").validate("SMOKE", "FAILED")
//            val results = branch.standardFilter(
//                    sinceValidationStamp = "SMOKE",
//                    sinceValidationStampStatus = "PASSED"
//            )
//            assertEquals(
//                    listOf("3", "2", "1"),
//                    results.map { it.name }
//            )
//        }
//    }
//
//}