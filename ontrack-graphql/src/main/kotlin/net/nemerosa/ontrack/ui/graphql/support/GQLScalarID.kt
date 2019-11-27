package net.nemerosa.ontrack.ui.graphql.support

import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import net.nemerosa.ontrack.model.structure.ID

class GQLScalarID private constructor() : GraphQLScalarType(
        "ID",
        "ID Type",
        object : Coercing<ID, Int> {

            override fun serialize(dataFetcherResult: Any): Int =
                    when (dataFetcherResult) {
                        is ID -> dataFetcherResult.value
                        is Number -> dataFetcherResult.toInt()
                        else -> throw CoercingSerializeException("Cannot convert $dataFetcherResult to an Int")
                    }

            override fun parseValue(input: Any): ID =
                    when (input) {
                        is Number -> ID.of(input.toInt())
                        else -> throw CoercingParseLiteralException("Cannot parse ID from $input")
                    }

            override fun parseLiteral(input: Any): ID = parseLiteral(input)

        }
) {
    companion object {
        @JvmField
        val INSTANCE: GraphQLScalarType = GQLScalarID()
    }
}