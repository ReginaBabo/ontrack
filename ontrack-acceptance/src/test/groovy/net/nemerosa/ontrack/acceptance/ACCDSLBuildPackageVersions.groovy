package net.nemerosa.ontrack.acceptance

import net.nemerosa.ontrack.acceptance.support.AcceptanceTestSuite
import org.junit.Test

import static net.nemerosa.ontrack.test.TestUtils.uid
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * Acceptance tests for the upload of packages for builds
 */
@AcceptanceTestSuite
class ACCDSLBuildPackageVersions extends AbstractACCDSL {

    @Test
    void 'Uploading package versions for a build using a properties file'() {
        // Project to use as reference
        def refName = uid('P')
        ontrack.project(refName) {
            setPackageIds(
                maven: [ "net.nemerosa.ontrack:ontrack-model" ],
                docker: [ "nemerosa/ontrack" ],
            )
            branch("master") {
                build("3.38.5")
            }
        }
        // Reference build
        def ref = ontrack.build(refName, "master", "3.38.5")

        // Parent project & build
        def parentName = uid("P")
        ontrack.project(parentName) {
            branch("master") {
                build("1.0.0") {
                    uploadPackageVersions ACCDSLBuildPackageVersions.class.getResource("/build/versions/upload.properties"), "application/properties"
                }
            }
        }

        // Gets the parent build and its versions
        def versions = ontrack.build(parentName, "master", "1.0.0").packageVersions

        def ontrackDep = versions.resources.get(0)
        assert ontrackDep.packageVersion.packageId.type.name == "Maven"
        assert ontrackDep.packageVersion.packageId.id == "net.nemerosa.ontrack:ontrack-model"
        assert ontrackDep.packageVersion.version == "3.38.5"
        assert ontrackDep.target != null: "Ref build has been found"
        assert ontrackDep.target.id == ref.id
        assert ontrackDep.target.name == "3.38.5"

        def springDep = versions.resources.get(1)
        assert springDep.packageVersion.packageId.type.name == "Maven"
        assert springDep.packageVersion.packageId.id == "org.springframework.boot:spring-boot-gradle-plugin"
        assert springDep.packageVersion.version == "1.5.14.RELEASE"
        assert springDep.target == null

    }

    // TODO Reference project using labels

}
