package net.nemerosa.ontrack.model.exceptions

class BuildPackageVersionUploadWrongMimeTypeException(
        type: String
) : InputException(
        """MIME type for the upload of package versions, "$type" is not supported."""
)
