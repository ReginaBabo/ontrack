#!/usr/bin/env bash
exec java \
    ${JAVA_OPTIONS} \
    ${CONFIG_OPTIONS} \
    -jar /ontrack/test/app/ontrack-kdsl-test.jar
