Neo4J Export
============

Launch the export and gets the ID:

```bash
export UUID=`curl --user admin:admin -X POST http://localhost:8080/extension/neo4j/export | jq -r .uuid`
```

Downloads the ZIP:

```bash
curl --user admin:admin http://localhost:8080/extension/neo4j/export/$UUID --output neo.zip
```

Unzips the files:

```bash
unzip neo.zip -d csv
```

Creates a database:

```bash
docker container run --rm \
    --volume `pwd`/csv:/var/lib/neo4j/import/csv \
    --volume `pwd`/db:/var/lib/neo4j/data/databases \
    neo4j:3.5.3 \
    neo4j-admin import \
    --database ontrack.db \
    --nodes /var/lib/neo4j/import/csv/node/Project.csv
```

> The database is created at `db/ontrack.db`

Move this database to the correct location:

```bash
rm -rf ~/neo4j/data/databases/ontrack.db
cp -r db/ontrack.db ~/neo4j/data/databases/
```

Now, run a Neo4J database from this database:

```bash
docker container run \
    --publish=7474:7474 --publish=7687:7687 \
    --detach \
    --volume=$HOME/neo4j/data:/data \
    --env=NEO4J_dbms_active__database=ontrack.db \
    --name=neo4j \
    neo4j:3.5.3
```