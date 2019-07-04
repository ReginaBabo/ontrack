package net.nemerosa.ontrack.extension.scm.support

import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.regex.Pattern

class TagPattern(
        val pattern: String
) {

    fun clone(replacementFunction: (String) -> String) = TagPattern(
            pattern = replacementFunction(pattern)
    )

    fun isValidTagName(name: String): Boolean {
        return StringUtils.isBlank(pattern) || createRegex().matcher(name).matches()
    }

    fun getBuildNameFromTagName(tagName: String): Optional<String> {
        return if (StringUtils.isBlank(pattern)) {
            Optional.of(tagName)
        } else {
            val matcher = createRegex().matcher(tagName)
            if (matcher.matches()) {
                if (matcher.groupCount() > 0) {
                    Optional.of(matcher.group(1))
                } else {
                    Optional.of(matcher.group(0))
                }
            } else {
                Optional.empty()
            }
        }
    }

    fun getTagNameFromBuildName(buildName: String): Optional<String> {
        if (StringUtils.isBlank(pattern)) {
            return Optional.of(buildName)
        } else {
            // Extraction of the build pattern, if any
            val buildPartRegex = "\\((.*\\*/*)\\)"
            val buildPartPattern = Pattern.compile(buildPartRegex)
            val buildPartMatcher = buildPartPattern.matcher(pattern)
            if (buildPartMatcher.find()) {
                val buildPart = buildPartMatcher.group(1)
                return if (Pattern.matches(buildPart, buildName)) {
                    val tag = StringBuffer()
                    do {
                        buildPartMatcher.appendReplacement(tag, buildName)
                    } while (buildPartMatcher.find())
                    buildPartMatcher.appendTail(tag)
                    Optional.of(tag.toString())
                } else {
                    Optional.empty()
                }
            } else return if (createRegex().matcher(buildName).matches()) {
                Optional.of(buildName)
            } else {
                Optional.empty()
            }
        }
    }

    private fun createRegex(): Pattern {
        return Pattern.compile(StringUtils.replace(pattern, "*", ".*"))
    }
}
