plugins {
    groovy
}

description = "DSL for Ontrack."

/**
 * Dependencies of the DSL module must be carefully controlled
 * outside of the core modules
 */

dependencies {
    compile("org.codehaus.groovy:groovy:2.5.8")
    compile("org.codehaus.groovy:groovy-json:2.5.8")
    compile("org.codehaus.groovy:groovy-templates:2.5.8")
    compile("org.slf4j:slf4j-api:1.7.25")
    compile("org.apache.httpcomponents:httpclient:4.5.3")
    compile("org.apache.httpcomponents:httpcore:4.4.6")
    compile("org.apache.httpcomponents:httpmime:4.5.3")
    compile("commons-logging:commons-logging:1.2")
    compile("net.jodah:failsafe:0.9.2")
    compile("com.fasterxml.jackson.core:jackson-databind:2.8.9")

    testCompile("junit:junit:4.12")
}

if (project.hasProperty("documentation")) {
    tasks.named<Jar>("javadocJar") {
        from("javadoc")
        from("groovydoc")
    }
}