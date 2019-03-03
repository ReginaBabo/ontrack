package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.common.Document
import net.nemerosa.ontrack.model.exceptions.BuildPackageVersionUploadParsingException
import net.nemerosa.ontrack.model.structure.BuildPackageVersionParser
import net.nemerosa.ontrack.model.structure.PackageService
import net.nemerosa.ontrack.model.structure.PackageType
import net.nemerosa.ontrack.model.structure.PackageVersion
import org.springframework.stereotype.Component

@Component
class PropertiesBuildPackageVersionParser(
        private val packageService: PackageService
) : BuildPackageVersionParser {

    companion object {
        private const val SEPARATOR = "::"
    }

    override val mimeTypes = setOf("text/plain", "text/properties", "application/properties", "application/octet-stream")

    override fun parsePackageVersions(defaultType: PackageType, doc: Document): List<PackageVersion> {
        val text = doc.content.toString(Charsets.UTF_8)
        val result = mutableListOf<PackageVersion>()
        text.lines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isNotBlank() && !trimmed.startsWith("#")) {
                val key = trimmed.substringBefore("=", "").trim()
                val version = trimmed.substringAfter("=", "").trim()
                if (key.isNotBlank() && version.isNotBlank()) {
                    result.add(parse(defaultType, key, version))
                }
            }
        }
        return result
    }

    private fun parse(defaultType: PackageType, key: String, value: String): PackageVersion {
        val typeName = key.substringBefore(SEPARATOR, "").trim()
        val id = key.substringAfter(SEPARATOR, key).trim()
        // Type
        val type: PackageType
        if (typeName.isBlank()) {
            type = defaultType
        } else {
            type = packageService.findByNameOrId(typeName) ?: throw BuildPackageVersionUploadParsingException("Type $typeName not found")
        }
        // OK
        return type.toId(id).toVersion(value.trim())
    }
}