package net.nemerosa.ontrack.acceptance

import net.nemerosa.ontrack.acceptance.support.AcceptanceTestSuite
import org.junit.Test

import static net.nemerosa.ontrack.test.TestUtils.uid
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

        // TODO Gets the parent build and its versions
        def versions = ontrack.build(parentName, "master", "1.0.0")
    }

    // TODO Reference project using labels

}
