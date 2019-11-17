package net.nemerosa.ontrack.kdsl.test.app

import org.springframework.stereotype.Component

/**
 * @property value Execution context
 * @property explicit Only when the context is explicitly set?
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class SpringTest(
        val value: Array<String> = [],
        val explicit: Boolean = false
)
