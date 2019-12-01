package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.graphql.AbstractGraphQLITSupport
import net.nemerosa.ontrack.model.labels.MainBuildLinksService
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.ProjectEntity
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractGeneralExtensionTestSupport : AbstractGraphQLITSupport() {


    @Autowired
    protected lateinit var mainBuildLinksService: MainBuildLinksService

    /**
     * Sets a message on an entity
     */
    protected fun ProjectEntity.message(
            text: String,
            type: MessageType = MessageType.INFO
    ) = property(
            MessagePropertyType::class,
            MessageProperty(type, text)
    )

    protected fun Project.setMainBuildLinksProperty(
            labels: List<String>,
            overrideGlobal: Boolean = false
    ) {
        setProperty(
                this,
                MainBuildLinksProjectPropertyType::class.java,
                MainBuildLinksProjectProperty(
                        labels,
                        overrideGlobal
                )
        )
    }
}