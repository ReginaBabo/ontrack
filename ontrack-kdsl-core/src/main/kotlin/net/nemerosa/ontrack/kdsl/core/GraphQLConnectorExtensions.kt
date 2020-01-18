package net.nemerosa.ontrack.kdsl.core

/**
 * GraphQL parameter complete declaration
 */
class GraphQLParamDecl(
        val name: String,
        val type: String
)

/**
 * Creating a parameter declaration
 */
infix fun String.type(type: String) = GraphQLParamDecl(this, type)

/**
 * GraphQL parameter complete implementation
 */
class GraphQLParamImpl(
        val name: String,
        val type: String,
        val value: Any?
)

/**
 * Creating a parameter implementation
 */
infix fun GraphQLParamDecl.value(value: Any?) = GraphQLParamImpl(this.name, this.type, value)
