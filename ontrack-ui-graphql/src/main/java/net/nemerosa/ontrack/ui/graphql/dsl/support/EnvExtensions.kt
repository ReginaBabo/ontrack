package net.nemerosa.ontrack.ui.graphql.dsl.support

import graphql.schema.DataFetchingEnvironment
import net.nemerosa.ontrack.json.asJson
import net.nemerosa.ontrack.json.parse
import kotlin.reflect.KProperty

inline fun <reified T> DataFetchingEnvironment.get(property: KProperty<T>): T =
        getArgument<T>(property.name)

inline fun <reified I : Any> jsonParser(): (DataFetchingEnvironment) -> I =
        { env: DataFetchingEnvironment ->
            env.arguments.asJson().parse()
        }