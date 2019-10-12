plugins {
    groovy
}

description = "Git client for Ontrack."

dependencies {
    compile("org.eclipse.jgit:org.eclipse.jgit:4.11.5.201810191925-r")
    compile("org.springframework:spring-tx")
    compile("commons-io:commons-io")
    compile("com.google.guava:guava")
    compile(project(":ontrack-common"))
    compile("org.codehaus.groovy:groovy")
    compile("org.slf4j:slf4j-api")
}
