package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.ProjectEntity
import kotlin.reflect.KClass

abstract class AbstractTypeProjectEntity<T : ProjectEntity>(
        type: KClass<T>
) : AbstractType<T>(type) {

    override fun dataFetchers(builder: TypeRuntimeWiring.Builder) {
        builder.field("id") { entity -> entity.id.value }
    }

}
