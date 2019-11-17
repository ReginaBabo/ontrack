package net.nemerosa.ontrack.kdsl.test.app

import java.util.concurrent.atomic.AtomicReference

/**
 * Holds the test context
 */
class SpringTestContext(
        val testDescription: String
) {
    companion object {
        val instance = AtomicReference<SpringTestContext>()
    }
}
