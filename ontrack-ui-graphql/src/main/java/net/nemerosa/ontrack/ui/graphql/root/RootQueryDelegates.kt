package net.nemerosa.ontrack.ui.graphql.root

import graphql.schema.DataFetchingEnvironment

object RootQueryDelegates {

    fun <I : Any, O : Any> rootQuery(
            parser: (DataFetchingEnvironment) -> I,
            code: (I) -> O
    ) = RootQueryProperty(parser, code)

}