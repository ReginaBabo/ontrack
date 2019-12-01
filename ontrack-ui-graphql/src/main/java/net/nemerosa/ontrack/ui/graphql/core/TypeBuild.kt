package net.nemerosa.ontrack.ui.graphql.core

import net.nemerosa.ontrack.model.structure.Build
import org.springframework.stereotype.Component

@Component
class TypeBuild : AbstractTypeProjectEntity<Build>(Build::class) {
}
