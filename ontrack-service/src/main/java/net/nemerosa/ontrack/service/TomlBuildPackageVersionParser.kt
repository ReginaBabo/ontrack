package net.nemerosa.ontrack.service

import com.github.jezza.Toml
import com.github.jezza.TomlTable
import net.nemerosa.ontrack.common.Document
import net.nemerosa.ontrack.model.exceptions.BuildPackageVersionUploadParsingException
import net.nemerosa.ontrack.model.structure.BuildPackageVersionParser
import net.nemerosa.ontrack.model.structure.PackageService
import net.nemerosa.ontrack.model.structure.PackageType
import net.nemerosa.ontrack.model.structure.PackageVersion
import org.springframework.stereotype.Component
import java.io.StringReader

@Component
class TomlBuildPackageVersionParser(
        private val packageService: PackageService
): BuildPackageVersionParser {
    override val mimeType: String = "application/toml"

    override fun parsePackageVersions(defaultType: PackageType, doc: Document): List<PackageVersion> {
        val table: TomlTable = try {
            Toml.from(StringReader(doc.content.toString(Charsets.UTF_8)))
        } catch (ex: Exception) {
            throw BuildPackageVersionUploadParsingException(ex)
        }
        return parsePackageVersions(defaultType, table)
    }

    private fun parsePackageVersions(type: PackageType, table: TomlTable): List<PackageVersion> {
        val result = mutableListOf<PackageVersion>()
        parseTableInto(result, type, table)
        return result.toList()
    }

    private fun parseTableInto(result: MutableList<PackageVersion>, type: PackageType, table: TomlTable) {
        table.forEach { key, value ->
            parseInto(result, type, key, value)
        }
    }

    private fun parseInto(result: MutableList<PackageVersion>, type: PackageType, key: String, value: Any) {
        when (value) {
            is TomlTable -> parseTypeInto(result, key, value)
            else -> parseIdInto(result, type, key, value.toString())
        }
    }

    private fun parseTypeInto(result: MutableList<PackageVersion>, typeName: String, values: TomlTable) {
        // Gets the type by name or ID
        val type: PackageType = packageService.findByNameOrId(typeName)
                ?: throw BuildPackageVersionUploadParsingException("Type $typeName not found")
        // Going on with the rest of the table
        parseTableInto(result, type, values)
    }

    private fun parseIdInto(result: MutableList<PackageVersion>, type: PackageType, id: String, version: String) {
        result.add(
                type.toId(id).toVersion(version)
        )
    }
}