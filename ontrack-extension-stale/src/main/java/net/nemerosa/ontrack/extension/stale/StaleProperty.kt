package net.nemerosa.ontrack.extension.stale

class StaleProperty(
        val disablingDuration: Int,
        val deletingDuration: Int,
        promotionsToKeep: List<String>?
) {

    /**
     * Kept for backward compatibility with old databases where this field was not set
     *
     * See `StatePropertyTest`
     */
    val promotionsToKeep: List<String> = promotionsToKeep ?: emptyList()

    companion object {
        @JvmStatic
        fun create(): StaleProperty {
            return StaleProperty(0, 0, emptyList())
        }
    }

    /**
     * Because of backward compatibility trick, the `promotionsToKeep` property cannot be declared
     * in the constructor as a `val` and the class cannot be flagged with `data`.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StaleProperty) return false

        if (disablingDuration != other.disablingDuration) return false
        if (deletingDuration != other.deletingDuration) return false
        if (promotionsToKeep != other.promotionsToKeep) return false

        return true
    }

    /**
     * Because of backward compatibility trick, the `promotionsToKeep` property cannot be declared
     * in the constructor as a `val` and the class cannot be flagged with `data`.
     */
    override fun hashCode(): Int {
        var result = disablingDuration
        result = 31 * result + deletingDuration
        result = 31 * result + promotionsToKeep.hashCode()
        return result
    }


}

