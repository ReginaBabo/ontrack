package net.nemerosa.ontrack.model.exceptions

class BuildPackageVersionUploadWrongMimeTypeException(
        expected: String,
        actual: String
) : InputException(
        """MIME type for the upload of package versions is expected to be "$expected" but was "$actual"."""
)
