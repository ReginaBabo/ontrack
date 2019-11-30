package net.nemerosa.ontrack.ui.graphql.dsl.support

import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KProperty

inline fun <reified T> DataFetchingEnvironment.get(property: KProperty<T>): T =
        getArgument<T>(property.name)
