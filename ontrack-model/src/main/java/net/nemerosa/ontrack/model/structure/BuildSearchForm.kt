package net.nemerosa.ontrack.model.structure

import javax.validation.constraints.Min
import javax.validation.constraints.Size

class BuildSearchForm {

    @Min(1)
    var maximumCount = 10
    var branchName: String? = null
    var buildName: String? = null
    var promotionName: String? = null
    var validationStampName: String? = null
    var property: String? = null
    @Size(max = 200)
    var propertyValue: String? = null
    var isBuildExactMatch: Boolean = false
    var linkedFrom: String? = null
    var linkedTo: String? = null

    constructor() {}

    constructor(
            maximumCount: Int,
            branchName: String?,
            buildName: String?,
            promotionName: String?,
            validationStampName: String?,
            property: String?,
            propertyValue: String?,
            buildExactMatch: Boolean,
            linkedFrom: String?,
            linkedTo: String?
    ) {
        this.maximumCount = maximumCount
        this.branchName = branchName
        this.buildName = buildName
        this.promotionName = promotionName
        this.validationStampName = validationStampName
        this.property = property
        this.propertyValue = propertyValue
        this.isBuildExactMatch = buildExactMatch
        this.linkedFrom = linkedFrom
        this.linkedTo = linkedTo
    }

    fun withMaximumCount(maximumCount: Int): BuildSearchForm {
        return BuildSearchForm(maximumCount, this.branchName, this.buildName, this.promotionName, this.validationStampName, this.property, this.propertyValue, this.isBuildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withBranchName(branchName: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, branchName, this.buildName, this.promotionName, this.validationStampName, this.property, this.propertyValue, this.isBuildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withBuildName(buildName: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, buildName, this.promotionName, this.validationStampName, this.property, this.propertyValue, this.isBuildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withPromotionName(promotionName: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, this.buildName, promotionName, this.validationStampName, this.property, this.propertyValue, this.isBuildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withValidationStampName(validationStampName: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, this.buildName, this.promotionName, validationStampName, this.property, this.propertyValue, this.isBuildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withProperty(property: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, this.buildName, this.promotionName, this.validationStampName, property, this.propertyValue, this.isBuildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withPropertyValue(propertyValue: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, this.buildName, this.promotionName, this.validationStampName, this.property, propertyValue, this.isBuildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withBuildExactMatch(buildExactMatch: Boolean): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, this.buildName, this.promotionName, this.validationStampName, this.property, this.propertyValue, buildExactMatch, this.linkedFrom, this.linkedTo)
    }

    fun withLinkedFrom(linkedFrom: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, this.buildName, this.promotionName, this.validationStampName, this.property, this.propertyValue, this.isBuildExactMatch, linkedFrom, this.linkedTo)
    }

    fun withLinkedTo(linkedTo: String): BuildSearchForm {
        return BuildSearchForm(this.maximumCount, this.branchName, this.buildName, this.promotionName, this.validationStampName, this.property, this.propertyValue, this.isBuildExactMatch, this.linkedFrom, linkedTo)
    }
}
