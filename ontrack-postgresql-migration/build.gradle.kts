apply(plugin = "org.springframework.boot")

dependencies {

    compile("org.springframework.boot:spring-boot-starter")
    compile("org.springframework.boot:spring-boot-starter-jdbc")
    compile("org.postgresql:postgresql")
    compile("org.flywaydb:flyway-core")
    compile("org.apache.commons:commons-lang3")

    runtime(project(":ontrack-database"))
    runtime("com.h2database:h2:1.4.197")

}
