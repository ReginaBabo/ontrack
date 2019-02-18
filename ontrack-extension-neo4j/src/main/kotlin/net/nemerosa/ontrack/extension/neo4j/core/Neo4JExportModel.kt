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

data class Neo4JPromotedBuild(
        val build: Int,
        // TODO Creation
        // TODO Creator
        val description: String,
        val promotion: String,
        val project: Int
) {
    val promotionUuid: String = Neo4JPromotion(project, promotion, "").uuid
}
