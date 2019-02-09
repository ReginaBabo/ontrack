package net.nemerosa.ontrack.model.structure

interface PackageService {

    fun getPackageType(type: String): PackageType?

}