package net.nemerosa.ontrack.graphql

import net.nemerosa.ontrack.json.toJson
import net.nemerosa.ontrack.test.TestUtils
import org.junit.Test

class PackageTypeGraphQLIT : AbstractQLKTITSupport() {

    @Test
    fun `Package types`() {
        val data = run("""{
                packageTypes {
                    id
                    name
                    description
                    feature {
                        id
                    }
                }
            }""")
        val type = data["packageTypes"].find { it["name"].asText() == "Test" }
        TestUtils.assertJsonEquals(
                mapOf(
                        "id" to "net.nemerosa.ontrack.it.TestPackageType",
                        "name" to "Test",
                        "description" to "Test package",
                        "feature" to mapOf(
                                "id" to "test"
                        )
                ).toJson(),
                type
        )
    }
}
