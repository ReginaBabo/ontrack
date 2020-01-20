plugins {
    `java-library`
}

apply(plugin = "org.springframework.boot")

val kotlinVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":ontrack-kdsl-impl"))
    implementation("args4j:args4j")

    implementation(kotlin("script-runtime", kotlinVersion))
    implementation(kotlin("compiler-embeddable", kotlinVersion))
    implementation(kotlin("scripting-compiler-embeddable", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))
}
