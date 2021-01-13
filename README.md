# Lunch Microservice

The service provides an endpoint that will determine, from a set of recipes, what I can have for lunch at a given date, based on my fridge ingredient's expiry date, so that I can quickly decide what I’ll be having to eat, and the ingredients required to prepare the meal.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database and insert data from lunch-data.sql script* 


### Run

1. Start database:

    ```
    docker-compose up -d
    ```
   
2. Add test data from  `sql/lunch-data.sql` to the database. Here's a helper script if you prefer:


    ```
    CONTAINER_ID=$(docker inspect --format="{{.Id}}" lunch-db)
    ```
    
    ```
    docker cp sql/lunch-data.sql $CONTAINER_ID:/lunch-data.sql
    ```
    
    ```
    docker exec $CONTAINER_ID /bin/sh -c 'mysql -u <MYSQL_USER> -p <MYSQL_ROOT_PASSWORD> lunch </lunch-data.sql'
    ```
    
3. Run Springboot LunchApplication


### Assumptions

    1. In order to prevent storing password in plain text in version control root password for database has been externalized MYSQL_ROOT_PASSWORD. ".env" file in main catalogue shoud contain this sensitive value for different environments.
    2. Tools that can ensure/force standard code formatting for codebase are omitted.
    3. API endpoints are not secured with spring security in order to avoid aditional confiuration and overhead.
    4. "Contract First" approach with OpenAPI document and interface generation was introduced to begin creation of production ready API containing simple anti-corruption layer.
