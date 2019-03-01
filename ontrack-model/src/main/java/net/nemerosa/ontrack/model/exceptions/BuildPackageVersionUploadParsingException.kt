package net.nemerosa.ontrack.model.exceptions

class BuildPackageVersionUploadParsingException(
        message: String?
) : InputException(
        "Parsing error: $message"
) {
    constructor(ex: Exception) : this(ex.message)
}
