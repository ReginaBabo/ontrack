#!/bin/bash

echo "Unzipping files"
rm -rf csv && unzip neo.zip -d csv

echo "Remove old database"
rm -rf db/ontrack.db

echo "Creates a database"
docker container run --rm \
    --volume `pwd`/csv:/var/lib/neo4j/import/csv \
    --volume `pwd`/db:/var/lib/neo4j/data/databases \
    neo4j:3.5.3 \
    neo4j-admin import \
    --nodes /var/lib/neo4j/import/csv/node/Project.csv \
    --nodes /var/lib/neo4j/import/csv/node/Branch.csv \
    --nodes /var/lib/neo4j/import/csv/node/Build.csv \
    --nodes /var/lib/neo4j/import/csv/node/Promotion.csv \
    --relationships /var/lib/neo4j/import/csv/rel/BRANCH_OF.csv \
    --relationships /var/lib/neo4j/import/csv/rel/BUILD_OF.csv \
    --relationships /var/lib/neo4j/import/csv/rel/DEPENDS_ON.csv \
    --relationships /var/lib/neo4j/import/csv/rel/PROMOTION_OF.csv \
    --database ontrack.db

echo "Move this database to the correct location"
rm -rf ~/neo4j/data/databases/ontrack.db
cp -r db/ontrack.db ~/neo4j/data/databases/

echo "Make sure the database is down"
docker container rm -fv neo4j

echo "Now, run a Neo4J database from this database"
docker container run \
    --publish=7474:7474 --publish=7687:7687 \
    --detach \
    --volume=$HOME/neo4j/data:/data \
    --env=NEO4J_dbms_active__database=ontrack.db \
    --name=neo4j \
    neo4j:3.5.3

echo "Database available at http://localhost:7474"
