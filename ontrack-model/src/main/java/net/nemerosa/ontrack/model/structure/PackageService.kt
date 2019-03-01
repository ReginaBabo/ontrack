package net.nemerosa.ontrack.model.structure

interface PackageService {

    val packageTypes: List<PackageType>

    fun getPackageType(type: String): PackageType?

    /**
     * Gets the default package type to use when not specified.
     */
    val defaultPackageType: PackageType

    /**
     * Similar to [getPackageType] but looks using either the [PackageType.id] or [PackageType.name] (this one
     * if a case insensitive way)
     */
    fun findByNameOrId(name: String): PackageType?

    /**
     * Converts a string having the format `type:id` into a genuine [PackageId].
     * If [errorOnParsingFailure] is `true` and if the i) the string cannot be parsed or ii) the
     * type is not found, an error is thrown. If `false`, `null` is returned instead.
     *
     * `null` is always returned if the string is either `null` or blank.
     */
    fun toPackageId(s: String?, errorOnParsingFailure: Boolean): PackageId?

    /**
     * Converts a string having the format `type:id:version` into a genuine [PackageVersion].
     * If [errorOnParsingFailure] is `true` and if the i) the string cannot be parsed or ii) the
     * type is not found, an error is thrown. If `false`, `null` is returned instead.
     *
     * `null` is always returned if the string is either `null` or blank.
     */
    fun toPackageVersion(s: String?, errorOnParsingFailure: Boolean): PackageVersion?

}