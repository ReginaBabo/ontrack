package net.nemerosa.ontrack.bdd

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [BDDConfig::class])
@EnableConfigurationProperties(BDDProperties::class)
class BDDConfig
