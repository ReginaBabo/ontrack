package net.nemerosa.ontrack.ui.graphql.dsl.root.mutation

import kotlin.reflect.KClass

object RootMutationDelegates {

    fun <I : Any, O : Any> rootMutation(
            input: Pair<String, KClass<I>>,
            code: (I) -> O
    ) = RootMutationProperty(input, code)

}