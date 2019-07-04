package net.nemerosa.ontrack.model.structure

import net.nemerosa.ontrack.model.buildfilter.StandardFilterDataBuilder
import java.time.LocalDate

data class StandardBuildFilterData(
        val count: Int = 0,
        val sincePromotionLevel: String? = null,
        val withPromotionLevel: String? = null,
        val afterDate: LocalDate? = null,
        val beforeDate: LocalDate? = null,
        val sinceValidationStamp: String? = null,
        val sinceValidationStampStatus: String? = null,
        val withValidationStamp: String? = null,
        val withValidationStampStatus: String? = null,
        val withProperty: String? = null,
        val withPropertyValue: String? = null,
        val sinceProperty: String? = null,
        val sincePropertyValue: String? = null,
        val linkedFrom: String? = null,
        val linkedFromPromotion: String? = null,
        val linkedTo: String? = null,
        val linkedToPromotion: String? = null
) : StandardFilterDataBuilder<StandardBuildFilterData> {

    override fun withSincePromotionLevel(sincePromotionLevel: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withWithPromotionLevel(withPromotionLevel: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withAfterDate(afterDate: LocalDate?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withBeforeDate(beforeDate: LocalDate?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withSinceValidationStamp(sinceValidationStamp: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withSinceValidationStampStatus(sinceValidationStampStatus: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withWithValidationStamp(withValidationStamp: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withWithValidationStampStatus(withValidationStampStatus: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withWithProperty(withProperty: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withWithPropertyValue(withPropertyValue: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withSinceProperty(sinceProperty: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withSincePropertyValue(sincePropertyValue: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withLinkedFrom(linkedFrom: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withLinkedFromPromotion(linkedFromPromotion: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withLinkedTo(linkedTo: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    override fun withLinkedToPromotion(linkedToPromotion: String?) = StandardBuildFilterData(
            count,
            sincePromotionLevel,
            withPromotionLevel,
            afterDate,
            beforeDate,
            sinceValidationStamp,
            sinceValidationStampStatus,
            withValidationStamp,
            withValidationStampStatus,
            withProperty,
            withPropertyValue,
            sinceProperty,
            sincePropertyValue,
            linkedFrom,
            linkedFromPromotion,
            linkedTo,
            linkedToPromotion
    )

    companion object {
        @JvmStatic
        fun of(count: Int) = StandardBuildFilterData(
                count = count,
                sincePromotionLevel = null,
                withPromotionLevel = null,
                afterDate = null, beforeDate = null,
                sinceValidationStamp = null,
                sinceValidationStampStatus = null,
                withValidationStamp = null,
                withValidationStampStatus = null,
                withProperty = null,
                withPropertyValue = null,
                sinceProperty = null,
                sincePropertyValue = null,
                linkedFrom = null,
                linkedFromPromotion = null,
                linkedTo = null,
                linkedToPromotion = null
        )
    }
}
