#!/bin/bash

export UUID=`curl --user admin:admin -X POST http://localhost:8080/extension/neo4j/export | jq -r .uuid`

curl --user admin:admin http://localhost:8080/extension/neo4j/export/$UUID --output neo.zip
