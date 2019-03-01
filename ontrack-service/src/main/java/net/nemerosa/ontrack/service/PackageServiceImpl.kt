package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.structure.PackageId
import net.nemerosa.ontrack.model.structure.PackageService
import net.nemerosa.ontrack.model.structure.PackageType
import net.nemerosa.ontrack.model.structure.PackageVersion
import org.springframework.stereotype.Service

@Service
class PackageServiceImpl(
        packageTypes: List<PackageType>
) : PackageService {

    private val index = packageTypes.associateBy { it.id }

    override val packageTypes: List<PackageType>
        get() = index.values.sortedBy { it.name }

    override fun getPackageType(type: String): PackageType? = index[type]

    override fun findByNameOrId(name: String): PackageType? =
            index[name] ?: index.values.find { it.name.equals(name, true) }

    override fun toPackageId(s: String?, errorOnParsingFailure: Boolean): PackageId? {
        if (s == null || s.isBlank()) {
            return null
        } else {
            val tokens = s.split(":")
            if (tokens.size != 2) {
                if (errorOnParsingFailure) {
                    throw IllegalArgumentException("Wrong format for package ID: $s")
                } else {
                    return null
                }
            } else {
                val typeName = tokens[0].trim()
                val id = tokens[1].trim()
                val type = getPackageType(typeName)
                return if (type == null) {
                    if (errorOnParsingFailure) {
                        throw IllegalArgumentException("Unknown package type: $typeName")
                    } else {
                        null
                    }
                } else {
                    PackageId(type, id)
                }
            }
        }
    }

    override fun toPackageVersion(s: String?, errorOnParsingFailure: Boolean): PackageVersion? {
        if (s == null || s.isBlank()) {
            return null
        } else {
            val tokens = s.split(":")
            if (tokens.size != 3) {
                if (errorOnParsingFailure) {
                    throw IllegalArgumentException("Wrong format for package version: $s")
                } else {
                    return null
                }
            } else {
                val typeName = tokens[0].trim()
                val id = tokens[1].trim()
                val version = tokens[2].trim()
                val type = getPackageType(typeName)
                return if (type == null) {
                    if (errorOnParsingFailure) {
                        throw IllegalArgumentException("Unknown package type: $typeName")
                    } else {
                        null
                    }
                } else if (version.isBlank()) {
                    if (errorOnParsingFailure) {
                        throw IllegalArgumentException("Version cannot be blank")
                    } else {
                        null
                    }
                } else {
                    PackageVersion(PackageId(type, id), version)
                }
            }
        }
    }
}