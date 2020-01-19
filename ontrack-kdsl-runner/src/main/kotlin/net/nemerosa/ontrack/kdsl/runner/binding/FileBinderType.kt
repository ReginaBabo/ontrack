package net.nemerosa.ontrack.kdsl.runner.binding

enum class FileBinderType(val fileBinder: FileBinder) {

    properties(PropertiesFileBinder()),

    json(JsonFileBinder())

}