# Base
FROM openjdk:8u222-jdk

# Directory for the test results
VOLUME /ontrack/bdd/output

# Root directory
RUN mkdir -p /ontrack/bdd/app
WORKDIR /ontrack/bdd/app

# Startup script
COPY bdd.sh /ontrack/bdd/app/
RUN chmod a+x /ontrack/bdd/app/bdd.sh

# General Java options
ENV JAVA_OPTIONS ''

# Test environment
# See BDDProperties
ENV BDD_MODEL_ONTRACK_URL="http://localhost:8080" \
    BDD_MODEL_ONTRACK_USERNAME="admin" \
    BDD_MODEL_ONTRACK_PASSWORD="admin"

# Serenity options
ENV SERENITY_OPTIONS ""

# Running the acceptance application
ENTRYPOINT /ontrack/bdd/app/bdd.sh

# Acceptance JAR
COPY ontrack-bdd-app.jar /ontrack/bdd/app/
