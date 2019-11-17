package net.nemerosa.ontrack.kdsl.test.app

class SpringTestConfig {

    private val CONTEXT_ALL = "all"

    /**
     * Tag for identifying the tests to run
     */
    var context: String? = null

    fun acceptTest(root: SpringTest?, member: SpringTest?): Boolean {
        return if (acceptTest(root)) {
            if (root != null && root.explicit) {
                // If no context is required on the member, it is accepted
                // Else checks the member
                member == null || acceptTest(member)
            } else {
                acceptTest(member)
            }
        } else {
            false
        }
    }

    /**
     * No context, all tests are eligible
     * If there is a context, but no annotation, the test cannot be accepted
     * There is a context *and* an annotation, checks the context is part of the
     * accepted values in the annotation
     */
    fun acceptTest(test: SpringTest?): Boolean {
        // Explicit check
        return if (test != null && test.explicit) {
            // The current context must be defined
            // and equal to one of the accepted contexts
            context != null && context in test.value
        } else {
            context.isNullOrBlank() || context == CONTEXT_ALL ||
                    (test != null && context in test.value)
        }
    }

}