# Base
FROM openjdk:8u222-jdk

# Directory for the test results
VOLUME /ontrack/bdd/output

RUN mkdir -p /ontrack/bdd/app

# Startup script
COPY bdd.sh /ontrack/bdd/app/
RUN chmod a+x /ontrack/bdd/app/bdd.sh

# Acceptance JAR
COPY ontrack-bdd-app.jar /ontrack/bdd/app/

# General Java options
ENV JAVA_OPTIONS ''

# Selenium URL
# Target Ontrack URL
# Target Ontrack admin password
# Target Ontrack SSL disabled?
# Acceptance test context
# Acceptance timeout
# Acceptance implicit wait
ENV ONTRACK_ACCEPTANCE_SELENIUM_GRID_URL='' \
    ONTRACK_ACCEPTANCE_SELENIUM_BROWSER_NAME='firefox' \
    ONTRACK_ACCEPTANCE_TARGET_URL='http://localhost:8080' \
    ONTRACK_ACCEPTANCE_TARGET_ADMIN_PASSWORD='admin' \
    ONTRACK_ACCEPTANCE_TARGET_SSL_DISABLED='false' \
    ONTRACK_ACCEPTANCE_CONTEXT='all' \
    ONTRACK_ACCEPTANCE_TIMEOUT='120' \
    ONTRACK_ACCEPTANCE_IMPLICIT_WAIT='5' \
    ONTRACK_ACCEPTANCE_INFLUXDB_URI='http://influxdb:8086'

# Running the acceptance application
ENTRYPOINT /ontrack/bdd/app/bdd.sh
