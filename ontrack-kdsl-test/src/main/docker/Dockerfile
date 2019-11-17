# Base
FROM openjdk:8u222-jdk

# Directory for the test results
VOLUME /ontrack/test/output

# Root directory
WORKDIR /ontrack/test/output

# Startup script
COPY kdsl-test.sh /ontrack/test/app/
RUN chmod a+x /ontrack/test/app/kdsl-test.sh

# General Java options
ENV JAVA_OPTIONS ''

# Test environment
# See BDDProperties
ENV BDD_MODEL_ONTRACK_URL="http://localhost:8080" \
    BDD_MODEL_ONTRACK_USERNAME="admin" \
    BDD_MODEL_ONTRACK_PASSWORD="admin"

# Configuration options
ENV CONFIG_OPTIONS ""

# Running the acceptance application
ENTRYPOINT /ontrack/test/app/kdsl-test.sh

# Test JAR
COPY ontrack-kdsl-test.jar /ontrack/test/app/
