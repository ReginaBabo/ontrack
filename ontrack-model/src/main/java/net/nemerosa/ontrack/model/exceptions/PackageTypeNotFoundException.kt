package net.nemerosa.ontrack.model.exceptions

class PackageTypeNotFoundException(name: String) : NotFoundException(
        """Package type with name "$name" cannot be found."""
)
