plugins {
    `java-library`
}

apply(plugin = "org.springframework.boot")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":ontrack-kdsl-impl"))
    implementation("args4j:args4j")
}
