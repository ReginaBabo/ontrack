package net.nemerosa.ontrack.model.structure

interface BranchModelMatcher {

    fun matches(branch: Branch): Boolean

}

infix fun BranchModelMatcher.and(other: BranchModelMatcher?): BranchModelMatcher =
        if (other == null) {
            this
        } else {
            object : BranchModelMatcher {
                override fun matches(branch: Branch): Boolean {
                    return this@and.matches(branch) || other.matches(branch)
                }
            }
        }
