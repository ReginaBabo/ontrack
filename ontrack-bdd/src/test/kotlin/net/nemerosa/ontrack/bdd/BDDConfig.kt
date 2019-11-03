package net.nemerosa.ontrack.bdd

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [BDDConfig::class])
class BDDConfig
