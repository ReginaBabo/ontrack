import net.nemerosa.ontrack.gradle.OntrackBoot15Plugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    groovy
}

apply<SpringBootPlugin>()

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.apache.commons:commons-lang3")

    runtimeOnly(project(":ontrack-database"))
    runtimeOnly("com.h2database:h2:1.4.196")
    runtimeOnly("org.hibernate:hibernate-validator")

}

/**
 * BOOT2 Workaround waiting for Spring Boot 2
 */

apply<OntrackBoot15Plugin>()
