plugins {
    `java-library`
}

val serenityVersion: String = "2.0.82"

dependencies {
    api("net.serenity-bdd:serenity-core:${serenityVersion}")
    api("net.serenity-bdd:serenity-spring:${serenityVersion}")
    api("net.serenity-bdd:serenity-cucumber:1.9.45")
    api("org.jetbrains.kotlin:kotlin-test")
    api(project(":ontrack-kdsl-impl"))
    api(project(":ontrack-test-support"))
    api("org.springframework.boot:spring-boot-starter")

    implementation("junit:junit")
    implementation("org.apache.commons:commons-lang3")
    implementation("args4j:args4j:2.33")
}
