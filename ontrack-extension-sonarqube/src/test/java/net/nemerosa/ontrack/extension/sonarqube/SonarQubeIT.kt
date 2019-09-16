package net.nemerosa.ontrack.extension.sonarqube

import net.nemerosa.ontrack.extension.sonarqube.configuration.SonarQubeConfiguration
import net.nemerosa.ontrack.extension.sonarqube.configuration.SonarQubeConfigurationService
import net.nemerosa.ontrack.extension.sonarqube.measures.SonarQubeMeasuresCollectionService
import net.nemerosa.ontrack.extension.sonarqube.measures.SonarQubeMeasuresSettings
import net.nemerosa.ontrack.extension.sonarqube.property.SonarQubeProperty
import net.nemerosa.ontrack.extension.sonarqube.property.SonarQubePropertyType
import net.nemerosa.ontrack.it.AbstractDSLTestSupport
import net.nemerosa.ontrack.model.security.GlobalSettings
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.test.TestUtils.uid
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SonarQubeIT : AbstractDSLTestSupport() {

    @Autowired
    private lateinit var sonarQubeConfigurationService: SonarQubeConfigurationService

    @Autowired
    private lateinit var sonarQubeMeasuresCollectionService: SonarQubeMeasuresCollectionService

    @Test
    fun `Launching the collection on validation run`() {
        withSonarQubeSettings {
            withConfiguredProject {
                branch {
                    val vs = validationStamp("sonarqube")
                    build("1.0.0") {
                        validate(vs)
                        // Checks that some SonarQube metrics are attached to this build
                        val measures = sonarQubeMeasuresCollectionService.getMeasures(this)
                        assertNotNull(measures) {
                            assertEquals(
                                    mapOf(
                                            "measure-1" to 12.3,
                                            "measure-2" to 45.0
                                    ),
                                    it.measures
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `Adding a new SonarQube configuration`() {
        withDisabledConfigurationTest {
            val name = uid("S")
            asUserWith<GlobalSettings> {
                val saved = sonarQubeConfigurationService.newConfiguration(
                        SonarQubeConfiguration(
                                name,
                                "https://sonarqube.nemerosa.net",
                                "my-ultra-secret-token"
                        )
                )
                assertEquals(name, saved.name)
                assertEquals("https://sonarqube.nemerosa.net", saved.url)
                assertEquals("", saved.password)
                // Gets the list of configurations
                val configs = sonarQubeConfigurationService.configurations
                // Checks we find the one we just created
                assertNotNull(
                        configs.find {
                            it.name == name
                        }
                )
                // Getting it by name
                val found = sonarQubeConfigurationService.getConfiguration(name)
                assertEquals(name, found.name)
                assertEquals("https://sonarqube.nemerosa.net", found.url)
                assertEquals("my-ultra-secret-token", found.password)
            }
        }
    }

    @Test
    fun `Project SonarQube property`() {
        withDisabledConfigurationTest {
            // Creates a configuration
            val name = uid("S")
            val configuration = createSonarQubeConfiguration(name)
            project {
                // Property
                setProperty(this, SonarQubePropertyType::class.java,
                        SonarQubeProperty(
                                configuration,
                                "my:key",
                                "sonarqube",
                                listOf("measure-1"),
                                false,
                                branchModel = true,
                                branchPattern = "master|develop"
                        )
                )
                // Gets the property back
                val property: SonarQubeProperty? = getProperty(this, SonarQubePropertyType::class.java)
                assertNotNull(property) {
                    assertEquals(name, it.configuration.name)
                    assertEquals("https://sonarqube.nemerosa.net", it.configuration.url)
                    assertEquals("my-ultra-secret-token", it.configuration.password)
                    assertEquals("my:key", it.key)
                    assertEquals("https://sonarqube.nemerosa.net/dashboard?id=my%3Akey", it.projectUrl)
                    assertEquals(listOf("measure-1"), it.measures)
                    assertEquals(false, it.override)
                    assertEquals(true, it.branchModel)
                    assertEquals("master|develop", it.branchPattern)
                }
            }
        }
    }

    @Test
    fun `Project property deleted when configuration is deleted`() {
        withDisabledConfigurationTest {
            val configuration = createSonarQubeConfiguration()
            project {
                // Sets the property
                setSonarQubeProperty(configuration, "my:key")
                // Deleting the configuration
                asAdmin {
                    sonarQubeConfigurationService.deleteConfiguration(configuration.name)
                }
                // Gets the property of the project
                val property: SonarQubeProperty? = getProperty(this, SonarQubePropertyType::class.java)
                assertNull(property, "Project property has been removed")
            }
        }
    }

    /**
     * Creating a new configuration
     */
    fun createSonarQubeConfiguration(name: String = uid("S")) =
            asUserWith<GlobalSettings, SonarQubeConfiguration> {
                sonarQubeConfigurationService.newConfiguration(
                        SonarQubeConfiguration(
                                name,
                                "https://sonarqube.nemerosa.net",
                                "my-ultra-secret-token"
                        )
                )
            }

    /**
     * Sets a project property
     */
    fun Project.setSonarQubeProperty(configuration: SonarQubeConfiguration, key: String, stamp: String = "sonarqube") {
        setProperty(this, SonarQubePropertyType::class.java,
                SonarQubeProperty(
                        configuration = configuration,
                        key = key,
                        validationStamp = stamp,
                        measures = emptyList(),
                        override = false,
                        branchModel = false,
                        branchPattern = null
                )
        )
    }

    /**
     * Testing with some SonarQube measures
     */
    fun withSonarQubeSettings(
            measures: List<String> = listOf("measure-1", "measure-2"),
            disabled: Boolean = false,
            code: () -> Unit
    ) {
        val settings = settingsService.getCachedSettings(SonarQubeMeasuresSettings::class.java)
        try {
            // Sets the settings
            asAdmin {
                settingsManagerService.saveSettings(
                        SonarQubeMeasuresSettings(measures, disabled)
                )
            }
            // Runs the code
            code()
        } finally {
            // Restores the initial settings (only in case of success)
            asAdmin {
                settingsManagerService.saveSettings(settings)
            }
        }
    }

    /**
     * Testing with a project configured for SonarQube
     */
    fun withConfiguredProject(key: String = "project:key", stamp: String = "sonarqube", code: Project.() -> Unit) {
        withDisabledConfigurationTest {
            val config = createSonarQubeConfiguration()
            project {
                setSonarQubeProperty(config, key, stamp)
                code()
            }
        }
    }

}