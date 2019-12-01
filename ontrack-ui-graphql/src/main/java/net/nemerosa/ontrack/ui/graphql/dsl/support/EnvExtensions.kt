package net.nemerosa.ontrack.ui.graphql.dsl.support

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.json.asJson
import net.nemerosa.ontrack.json.parse
import kotlin.reflect.KProperty

inline fun <reified T> DataFetchingEnvironment.get(property: KProperty<T>): T =
        getArgument<T>(property.name)

inline operator fun <reified T> Map<String, Any>.get(property: KProperty<T>): T =
        get(property.name) as T

fun DataFetchingEnvironment.input(property: String): Map<String, Any> =
        getArgument(property)

inline fun <reified I : Any> jsonParser(): (DataFetchingEnvironment) -> I =
        { env: DataFetchingEnvironment ->
            env.arguments.asJson().parse()
        }