#!/usr/bin/env bash
exec java \
    ${JAVA_OPTIONS} \
    ${SERENITY_OPTIONS} \
    -jar /ontrack/bdd/app/ontrack-bdd-app.jar
