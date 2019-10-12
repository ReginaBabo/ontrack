description = "Generic customisable HTTP and JSON client."

dependencies {
    compile(project(":ontrack-common"))
    compile(project(":ontrack-json"))
    compile("org.apache.httpcomponents:httpclient")
    compile("org.apache.httpcomponents:httpmime")
    compile("org.slf4j:slf4j-api")
}
