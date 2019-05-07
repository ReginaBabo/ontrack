description = "Generic customisable HTTP and JSON client."

plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-common"))
    api(project(":ontrack-json"))
    api("org.apache.httpcomponents:httpclient")

    implementation("org.apache.httpcomponents:httpmime")
    implementation("org.slf4j:slf4j-api")
    implementation("org.apache.commons:commons-lang3")
}