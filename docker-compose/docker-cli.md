## docker-compose

up : docker-compose -f [docker-compose file name] up
down : docker-compose -f [docker-compose file name] down

## ssh into container

docker exec -it [container-id] /bin/bash