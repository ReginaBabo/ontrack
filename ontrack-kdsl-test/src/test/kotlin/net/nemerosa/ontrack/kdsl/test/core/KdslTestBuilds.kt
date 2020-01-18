package net.nemerosa.ontrack.kdsl.test.core

//import net.nemerosa.ontrack.kdsl.model.deprecated.build
//import net.nemerosa.ontrack.kdsl.model.deprecated.createBuild
//import net.nemerosa.ontrack.kdsl.model.deprecated.nextBuild
//import net.nemerosa.ontrack.kdsl.model.deprecated.previousBuild
//import net.nemerosa.ontrack.kdsl.test.app.SpringTest
//import net.nemerosa.ontrack.kdsl.test.support.AbstractKdslTest
//import org.junit.Test
//import org.springframework.web.client.HttpClientErrorException
//import kotlin.test.assertEquals
//import kotlin.test.assertFailsWith
//import kotlin.test.assertNull
//
//@SpringTest
//class KdslTestBuilds : AbstractKdslTest() {
//
//    @Test
//    fun `Build previous and next`() {
//        // Project and branch
//        branch {
//            val build = (1..3).map {
//                build("$it")
//            }
//            // Build 1 has no previous build
//            assertNull(build[0].previousBuild)
//            assertEquals("2", build[0].nextBuild?.name)
//            // Build 2 has previous and next builds
//            assertEquals("1", build[1].previousBuild?.name)
//            assertEquals("3", build[1].nextBuild?.name)
//            // Build 3 has no next build
//            assertEquals("2", build[2].previousBuild?.name)
//            assertNull(build[2].nextBuild)
//        }
//    }
//
//    @Test
//    fun `Build update`() {
//        branch {
//            // Creates a build
//            build("1", "Build 1")
//            // Updates the build
//            val build = build("1", "Build 2")
//            assertEquals("Build 2", build.description)
//        }
//    }
//
//    @Test
//    fun `Build creation twice`() {
//        branch {
//            // Creates a build
//            build("1", "Build 1")
//            // Creates the build a second time
//            assertFailsWith<HttpClientErrorException.BadRequest> {
//                createBuild("1", "Build 2")
//            }
//        }
//    }
//}