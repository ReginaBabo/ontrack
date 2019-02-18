package net.nemerosa.ontrack.model.security

import java.util.function.Supplier

/**
 * Given a [function][call] which returns an object, returns _another_ function
 * which wraps it into a secure call. The returned function can be called
 * asynchronously.
 *
 * @see SecurityService.runAsAdmin
 */
fun <T> SecurityService.functionAsAdmin(call: () -> T): () -> T =
        { runAsAdmin(Supplier { call() }).get() }
