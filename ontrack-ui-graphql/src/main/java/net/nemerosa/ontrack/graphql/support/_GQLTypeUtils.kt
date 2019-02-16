package net.nemerosa.ontrack.graphql.support

import graphql.Scalars.GraphQLInt
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.schema.GQLType

typealias TypeBuilder = graphql.schema.GraphQLObjectType.Builder

fun TypeBuilder.intField(name: String, description: String): GraphQLObjectType.Builder =
        field { it.name(name).description(description).type(GraphQLInt) }

fun TypeBuilder.stringField(name: String, description: String): GraphQLObjectType.Builder =
        field {
            it.name(name).description(description).type(GraphQLString)
        }

fun TypeBuilder.refField(name: String, description: String, type: GQLType): GraphQLObjectType.Builder =
        field {
            it.name(name).description(description)
                    .type(type.typeRef)
        }
