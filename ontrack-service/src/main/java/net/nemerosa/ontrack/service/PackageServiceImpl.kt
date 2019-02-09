package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.structure.PackageService
import net.nemerosa.ontrack.model.structure.PackageType
import org.springframework.stereotype.Service

@Service
class PackageServiceImpl(
        packageTypes: List<PackageType>
) : PackageService {

    private val index = packageTypes.associateBy { it.id }

    override fun getPackageType(type: String): PackageType? = index[type]

}