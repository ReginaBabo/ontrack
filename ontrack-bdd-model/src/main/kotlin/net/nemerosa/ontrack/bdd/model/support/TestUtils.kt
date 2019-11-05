package net.nemerosa.ontrack.bdd.model.support

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

private val counter = AtomicInteger()

fun uid(prefix: String = ""): String = prefix + SimpleDateFormat("mmssSSS").format(Date()) + counter.incrementAndGet()
