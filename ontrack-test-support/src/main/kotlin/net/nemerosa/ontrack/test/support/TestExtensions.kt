package net.nemerosa.ontrack.test.support

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertTrue
import kotlin.test.fail

private val counter = AtomicInteger()

fun uid(prefix: String = ""): String = prefix + SimpleDateFormat("mmssSSS").format(Date()) + counter.incrementAndGet()

inline fun <reified T> assertIs(value: Any?, code: (T) -> Unit) {
    if (value is T) {
        code(value)
    } else {
        fail("Not a ${T::class.qualifiedName}")
    }
}

fun <T> assertPresent(o: Optional<T>, message: String = "Optional must be present", code: (T) -> Unit) {
    if (o.isPresent) {
        code(o.get())
    } else {
        fail(message)
    }
}

fun <T> assertNotPresent(o: Optional<T>, message: String = "Optional is not present") {
    if (o.isPresent) {
        fail(message)
    }
}

fun <T> assertAll(c: Collection<T>, check: (T) -> Boolean) = assertTrue(c.all(check))
