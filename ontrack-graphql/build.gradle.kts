plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-ui-support"))
    api("com.graphql-java:graphql-java")

    // TODO Extracts classes in support
    implementation(project(":ontrack-ui-graphql"))

    implementation("com.graphql-java:graphql-java-spring-boot-starter-webmvc:1.0")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.apache.commons:commons-lang3")
}
