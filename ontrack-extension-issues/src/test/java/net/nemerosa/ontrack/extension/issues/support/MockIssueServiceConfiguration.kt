package net.nemerosa.ontrack.extension.issues.support

import net.nemerosa.ontrack.extension.issues.export.IssueExportServiceFactory
import net.nemerosa.ontrack.extension.issues.model.ConfiguredIssueService
import net.nemerosa.ontrack.extension.issues.model.IssueServiceConfiguration
import org.mockito.Mockito

class MockIssueServiceConfiguration(
        private val name: String
) : IssueServiceConfiguration {

    companion object {
        @JvmField
        val INSTANCE = MockIssueServiceConfiguration("default")

        @JvmStatic
        fun configuredIssueService(name: String) = ConfiguredIssueService(
                MockIssueServiceExtension(
                        MockIssueServiceFeature(),
                        Mockito.mock(IssueExportServiceFactory::class.java)
                ),
                MockIssueServiceConfiguration(name)
        )
    }

    override fun getServiceId(): String = "mock"

    override fun getName(): String = name

}
