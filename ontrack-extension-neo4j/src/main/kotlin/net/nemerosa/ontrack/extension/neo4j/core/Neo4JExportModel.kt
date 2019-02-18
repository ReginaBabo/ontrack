package net.nemerosa.ontrack.extension.neo4j.core

data class Neo4JBuildLink(
        val from: Int,
        val to: Int
)

data class Neo4JPromotion(
        val project: Int,
        val name: String,
        val description: String
) {
    val uuid: String = "$project-$name"
}
