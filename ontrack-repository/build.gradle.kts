plugins {
    `java-library`
}

dependencies {
    api(project(":ontrack-model"))

    implementation("com.google.guava:guava") // TODO: remove (Nullable dependency)
}