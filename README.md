# cienema
    docker compose up -d
# hiconnecta @base
    psql -h localhost -U postgres -p 1111 -d cinema_db
    copiena ilay script ao @table.sql

# run
    mvn sprin-boot:run

# anarana branche atao oh hoe:
    feat/client-saisie
    de manao pull request fa tsy tonde push any @main

# nb
    efa misy code mba mazava2 ny ao de mba ataov mifanaraka


# dump place
docker exec -t pg_cinema pg_dump -U postgres -d cinema_db > ./sql/dumps/place_db.sql


# dump pub
docker exec -t pg_cinema pg_dump -U postgres -d cinema_db > ./sql/dumps/pub_db.sql




# importer
docker exec -i pg_cinema psql -U postgres -d cinema_db < ./sql/dumps/cinema_db.sql
