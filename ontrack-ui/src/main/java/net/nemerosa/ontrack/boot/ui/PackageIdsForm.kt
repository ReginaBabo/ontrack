package net.nemerosa.ontrack.boot.ui

class PackageIdsForm(
        val packages: List<PackageIdsFormItem>
) {

    class PackageIdsFormItem(
            val type: String,
            val id: String
    )

}