plugins {
    groovy
}

dependencies {

    compile(project(":ontrack-common"))
    compile(project(":ontrack-json"))
    compile(project(":ontrack-job"))
    api("com.google.guava:guava")
    api("org.apache.commons:commons-text")
    api("org.springframework:spring-context")
    api("org.springframework.security:spring-security-core")
    api("javax.validation:validation-api")
    api("org.slf4j:slf4j-api")
    api("org.springframework.boot:spring-boot-starter-actuator")

    testApi("org.codehaus.groovy:groovy-all")
    testApi(project(":ontrack-test-utils"))

}