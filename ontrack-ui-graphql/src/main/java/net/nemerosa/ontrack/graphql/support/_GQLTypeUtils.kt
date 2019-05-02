package net.nemerosa.ontrack.graphql.support

import graphql.Scalars.GraphQLInt
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLObjectType
import net.nemerosa.ontrack.graphql.schema.GQLType
import net.nemerosa.ontrack.graphql.support.GraphqlUtils.stdList

typealias TypeBuilder = graphql.schema.GraphQLObjectType.Builder

fun TypeBuilder.intField(name: String, description: String): GraphQLObjectType.Builder =
        field { it.name(name).description(description).type(GraphQLInt) }

fun TypeBuilder.stringField(name: String, description: String): GraphQLObjectType.Builder =
        field {
            it.name(name).description(description).type(GraphQLString)
        }

fun TypeBuilder.objectField(type: GQLType, name: String, description: String): GraphQLObjectType.Builder =
        field {
            it.name(name).description(description).type(type.typeRef)
        }

fun TypeBuilder.objectArrayField(type: GQLType, name: String, description: String): GraphQLObjectType.Builder =
        field {
            it.name(name).description(description).type(stdList(type.typeRef))
        }

fun TypeBuilder.creationField(name: String, description: String): GraphQLObjectType.Builder =
        field {
            it.name(name).description(description).type(GraphQLString)
        }
