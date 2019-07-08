plugins {
    `java-library`
    groovy
}

dependencies {
    implementation(project(":ontrack-model"))
    implementation(project(":ontrack-repository"))
    implementation(project(":ontrack-extension-api"))
    implementation(project(":ontrack-job"))
    implementation("com.google.guava:guava")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-ldap")
    implementation("org.slf4j:slf4j-api")
    implementation("commons-io:commons-io")
    implementation("org.codehaus.groovy:groovy-all")
    implementation("org.kohsuke:groovy-sandbox") {
        exclude(group = "org.codehaus.groovy")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.jgrapht:jgrapht-core")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.flywaydb:flyway-core")

    implementation("io.micrometer:micrometer-spring-legacy")

    testImplementation(project(":ontrack-it-utils"))
    testImplementation(project(path = ":ontrack-extension-api", configuration = "tests"))
    testImplementation(project(":ontrack-repository-impl"))

    testRuntimeOnly("io.micrometer:micrometer-registry-prometheus")

//    // Dependencies needed for LDAP integration tests
//    def apacheDsVersion = "1.5.5"
//    testCompile("org.apache.directory.server:apacheds-all:${apacheDsVersion}") {
//        exclude group: "org.slf4j", module: "slf4j-api"
//    }

}
