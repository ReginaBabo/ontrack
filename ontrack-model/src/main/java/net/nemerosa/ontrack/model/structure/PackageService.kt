package net.nemerosa.ontrack.model.structure

interface PackageService {

    val packageTypes: List<PackageType>

    fun getPackageType(type: String): PackageType?

    /**
     * Converts a string having the format `type:id` into a genuine [PackageId].
     * If [errorOnParsingFailure] is `true` and if the i) the string cannot be parsed or ii) the
     * type is not found, an error is thrown. If `false`, `null` is returned instead.
     *
     * `null` is always returned if the string is either `null` or blank.
     */
    fun toPackageId(s: String?, errorOnParsingFailure: Boolean): PackageId?

}