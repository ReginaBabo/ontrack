package net.nemerosa.ontrack.kdsl.model


/**
 * Creates or gets a promotion level and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the promotion level
 * @param description Description of the promotion level
 * @param initFn Code to run against the created promotion level
 * @return Object return by [initFn]
 */
fun <T> Branch.promotionLevel(
        name: String,
        description: String = "",
        initFn: PromotionLevel.() -> T
): T {
    val pl = promotionLevels.firstOrNull { it.name == name } ?: createPromotionLevel(name, description)
    return pl.initFn()
}

/**
 * Creates or gets a promotion level.
 *
 * @param name Name of the promotion level
 * @param description Description of the promotion level
 * @return Created or retrieved promotion level
 */
fun Branch.promotionLevel(
        name: String,
        description: String = ""
): PromotionLevel = promotionLevel(name, description) { this }

/**
 * Creates or gets a validation stamp and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the validation stamp
 * @param description Description of the validation stamp
 * @param initFn Code to run against the created validation stamp
 * @return Object return by [initFn]
 */
fun <T> Branch.validationStamp(
        name: String,
        description: String = "",
        initFn: ValidationStamp.() -> T
): T {
    val vs = validationStamps(name = name).firstOrNull() ?: createValidationStamp(name, description)
    return vs.initFn()
}

/**
 * Creates or gets a validation stamp.
 *
 * @param name Name of the validation stamp
 * @param description Description of the validation stamp
 * @return Created or retrieved validation stamp
 */
fun Branch.validationStamp(
        name: String,
        description: String = ""
): ValidationStamp = validationStamp(name, description) { this }


/**
 * Creates or gets a build and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the build
 * @param description Description of the build
 * @param initFn Code to run against the created build
 * @return Object return by [initFn]
 */
fun <T> Branch.build(
        name: String,
        description: String = "",
        initFn: Build.() -> T
): T {
    val build: Build? = project.searchBuilds(
            branchName = this.name,
            buildName = name,
            buildExactMatch = true
    ).firstOrNull()
    val actualBuild = if (build != null) {
        build.update(name, description)
        ontrack.getBuildByID(build.id)
    } else {
        createBuild(name, description)
    }
    return actualBuild.initFn()
}

/**
 * Creates or gets a build
 *
 * @param name Name of the build
 * @param description Description of the build
 * @return Creates build
 */
fun Branch.build(
        name: String,
        description: String = ""
) = build(name, description) { this }
