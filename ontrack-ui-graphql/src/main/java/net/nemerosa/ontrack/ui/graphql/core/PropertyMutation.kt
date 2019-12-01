package net.nemerosa.ontrack.ui.graphql.core

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.model.structure.ProjectEntity
import net.nemerosa.ontrack.model.structure.PropertyService
import net.nemerosa.ontrack.model.structure.PropertyType
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.root.mutation.AbstractTypedRootMutationGraphQLContributor
import net.nemerosa.ontrack.ui.graphql.dsl.support.parse
import kotlin.reflect.KClass

abstract class PropertyMutation<T, P : PropertyType<T>>(
        override val name: String,
        private val propertyType: KClass<P>,
        private val structureService: StructureService,
        private val propertyService: PropertyService
) : AbstractTypedRootMutationGraphQLContributor<PropertyMutationInput<T>, ProjectEntity>() {

    override fun parseInput(env: DataFetchingEnvironment): PropertyMutationInput<T> = env.parse()

    override fun getValue(input: PropertyMutationInput<T>): ProjectEntity {
        val entity = input.entity.loadProjectEntity(structureService)
        val value = input.value
        propertyService.editProperty(
                entity,
                propertyType.java,
                value
        )
        return entity
    }
}

data class PropertyMutationInput<T>(
        val entity: ProjectEntityInput,
        val value: T
)
