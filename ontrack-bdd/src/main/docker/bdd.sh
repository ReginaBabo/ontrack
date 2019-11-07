#!/usr/bin/env bash
exec java \
    ${JAVA_OPTIONS} \
    -Dwebdriver.base.url=${BDD_MODEL_ONTRACK_URL} \
    ${SERENITY_OPTIONS} \
    -jar /ontrack/bdd/app/ontrack-bdd-app.jar
