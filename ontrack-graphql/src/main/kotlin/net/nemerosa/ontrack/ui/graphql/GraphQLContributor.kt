package net.nemerosa.ontrack.ui.graphql

import graphql.schema.idl.RuntimeWiring

interface GraphQLContributor {

    fun wire(wiring: RuntimeWiring.Builder)

}