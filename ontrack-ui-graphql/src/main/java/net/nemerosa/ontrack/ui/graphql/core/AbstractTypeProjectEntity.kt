package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.idl.TypeRuntimeWiring
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.ui.graphql.support.AbstractType
import net.nemerosa.ontrack.ui.graphql.support.TypeFieldContributor
import kotlin.reflect.KClass

abstract class AbstractTypeProjectEntity<T : ProjectEntity>(
        type: KClass<T>,
        typeFieldContributors: List<TypeFieldContributor>
) : AbstractType<T>(type, typeFieldContributors) {

    override fun dataFetchers(builder: TypeRuntimeWiring.Builder) {
        super.dataFetchers(builder)

        builder.field("id") { entity -> entity.id.value }

    }


}
