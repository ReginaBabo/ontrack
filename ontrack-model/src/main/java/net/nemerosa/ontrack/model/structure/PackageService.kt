package net.nemerosa.ontrack.model.structure

interface PackageService {

    val packageTypes: List<PackageType>

    fun getPackageType(type: String): PackageType?

}