package net.nemerosa.ontrack.model.structure

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class BranchModelMatcherTest {

    private val project = Project.of(NameDescription.nd("P", ""))
    private val branchA = Branch.of(project, NameDescription.nd("a", ""))
    private val branchB = Branch.of(project, NameDescription.nd("b", ""))

    private val matcherA = object : BranchModelMatcher {
        override fun matches(branch: Branch): Boolean = branch.name.startsWith("a")
    }

    private val matcherB = object : BranchModelMatcher {
        override fun matches(branch: Branch): Boolean = branch.name.startsWith("b")
    }

    @Test
    fun `and with null`() {
        val matcher = matcherA.and(null)
        assertSame(matcherA, matcher)
        assertTrue(matcher.matches(branchA))
        assertFalse(matcher.matches(branchB))
    }

    @Test
    fun and() {
        val matcher = matcherA and matcherB
        assertTrue(matcher.matches(branchA))
        assertTrue(matcher.matches(branchB))
    }

}