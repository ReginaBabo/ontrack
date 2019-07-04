package net.nemerosa.ontrack.extension.svn.support

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import net.nemerosa.ontrack.extension.scm.support.TagPattern
import net.nemerosa.ontrack.extension.svn.service.SVNService
import net.nemerosa.ontrack.json.ObjectMapperFactory
import net.nemerosa.ontrack.model.exceptions.JsonParsingException
import net.nemerosa.ontrack.model.form.Form
import net.nemerosa.ontrack.model.form.Text
import net.nemerosa.ontrack.model.structure.StructureService
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

/**
 * Build / revision relationship based on the build name being a subversion tag name which must follow a given pattern.
 */
@Component
class TagNamePatternSvnRevisionLink(
        svnService: SVNService,
        structureService: StructureService
) : AbstractTagBasedSvnRevisionLink<TagPattern>(svnService, structureService) {

    public override fun getBuildName(data: TagPattern, tagName: String): Optional<String> {
        return if (data.isValidTagName(tagName)) {
            data.getBuildNameFromTagName(tagName)
        } else {
            Optional.empty()
        }
    }

    override fun getId(): String {
        return "tagPattern"
    }

    override fun getName(): String {
        return "Tag pattern as name"
    }

    override fun clone(data: TagPattern, replacementFunction: Function<String, String>): TagPattern {
        return data.clone {
            replacementFunction.apply(it)
        }
    }

    override fun parseData(node: JsonNode): TagPattern {
        try {
            return ObjectMapperFactory.create().treeToValue(node, TagPattern::class.java)
        } catch (e: JsonProcessingException) {
            throw JsonParsingException(e)
        }

    }

    override fun toJson(data: TagPattern): JsonNode {
        return ObjectMapperFactory.create().valueToTree(data)
    }

    override fun getForm(): Form {
        return Form.create()
                .with(
                        Text.of("pattern")
                                .label("Tag pattern")
                                .help("@file:extension/svn/buildRevisionLink/tagPattern.help.tpl.html")
                )
    }

    override fun isValidBuildName(data: TagPattern, name: String): Boolean {
        return data.isValidTagName(name)
    }

    public override fun getTagName(data: TagPattern, buildName: String): Optional<String> {
        return data.getTagNameFromBuildName(buildName)
    }
}
