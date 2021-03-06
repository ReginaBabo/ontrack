package net.nemerosa.ontrack.dsl.properties

import net.nemerosa.ontrack.dsl.Ontrack
import net.nemerosa.ontrack.dsl.PromotionLevel
import net.nemerosa.ontrack.dsl.doc.DSL
import net.nemerosa.ontrack.dsl.doc.DSLMethod
import net.nemerosa.ontrack.dsl.doc.DSLProperties

@DSL
@DSLProperties
class PromotionLevelProperties extends ProjectEntityProperties {

    private final PromotionLevel promotionLevel

    PromotionLevelProperties(Ontrack ontrack, PromotionLevel promotionLevel) {
        super(ontrack, promotionLevel)
        this.promotionLevel = promotionLevel
    }

    /**
     * Promotion dependencies
     */

    @DSLMethod("Sets the validation stamps participating into the auto promotion.")
    void setPromotionDependencies(List<String> promotions) {
        property('net.nemerosa.ontrack.extension.general.PromotionDependenciesPropertyType', [
                dependencies: promotions,
        ])
    }

    @DSLMethod("Gets the validation stamps participating into the auto promotion. The returned list can be null if the property is not defined.")
    List<String> getPromotionDependencies() {
        def value = property('net.nemerosa.ontrack.extension.general.PromotionDependenciesPropertyType', false)
        return value ? value.dependencies as List<String> : null
    }

    /**
     * Auto promotion
     */

    @DSLMethod("Sets the validation stamps participating into the auto promotion.")
    def autoPromotion(String... validationStamps) {
        autoPromotion(validationStamps as List)
    }

    @DSLMethod(id="auto-promotion-patterns", value = "Sets the validation stamps participating into the auto promotion, and sets the include/exclude settings.", count = 3)
    def autoPromotion(Collection<String> validationStamps, String include = '', String exclude = '') {
        property(
                'net.nemerosa.ontrack.extension.general.AutoPromotionPropertyType',
                [
                        validationStamps: validationStamps.collect {
                            String vsName -> ontrack.validationStamp(promotionLevel.project, promotionLevel.branch, vsName).id
                        },
                        include         : include,
                        exclude         : exclude,
                ]
        )
    }

    @DSLMethod("Checks if the promotion level is set in auto promotion.")
    boolean getAutoPromotion() {
        property('net.nemerosa.ontrack.extension.general.AutoPromotionPropertyType')
    }

}
