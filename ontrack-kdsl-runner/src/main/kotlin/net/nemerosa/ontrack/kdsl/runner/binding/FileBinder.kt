package net.nemerosa.ontrack.kdsl.runner.binding

import java.io.File

interface FileBinder {

    fun readBindings(file: File): Map<String, Any?>

}