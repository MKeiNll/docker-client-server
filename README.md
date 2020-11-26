# docker-client-server

A sample client/server application.
Both parts are made on Spring Boot.

## Features

+ Server
  + Offers service for player wallet (balance) management.
    + Balance cannot go less than 0.
    + Balance cannot go bigger than the configured limit.
    + In the case of duplicate transaction IDs, a previous response is returned. Only the latest 1000 transactions are taken into consideration.
    + All transactions are denied for a configured list of blacklisted players.
  + Writes information about every transaction into a log file.
  + Balance state is managed in-memory & backed up in an embedded HSQLDB database.
  + Balance state is loaded from the database into the memory on application startup.
  + There is a periodical background process (executing every minute) to back up the player state from memory into the database.
  + Player records are being created on demand.
+ Client
  + Is a server offering gameplay logic.
  + Specific gameplay logic is not implemented & is simulated via random balance change generation instead.
+ Communication between client & server is done via HTTP. There are 2 endpoints - `/addFunds` & `/withdrawFunds`.
  + Request model is `username`, `transactionId` & `balanceChange`.
  + Response model is `transactionId`, `errorCode`, `balanceVersion`, `balanceChange` & `balanceAfterChange`.
+ Database consists of 1 table - `PLAYER(USERNAME, BALANCE_VERSION, BALANCE)`.
+ Both applications are built using Gradle & are packaged into Docker containers.

## Configuration (`main/java/resources/application.properties`)

+ Server:
  + `server.port` - sets the port of the server
  + `logging.level.org.example` - sets the logging level
  + `logging.file.name` - sets the log file name
  + `game.max-balance` - sets the maximum balance a player can reach
  + `game.blacklist` - a comma-separated list of blacklisted player usernames
+ Client:
  + `server.port` - sets the port of the client
  + `game.server-url` - sets the url of the server to send requests to
  
## Running the app

+ Server:
  + `cd server`
  + `./gradlew bootBuildImage` (or `gradlew bootBuildImage on Windows`)
  + `docker run -p <port>:<port> game-server`, where `<port>` is a value set up in the configuration file
+ Client:
  + `cd client`
  + `./gradlew bootBuildImage` (or `gradlew bootBuildImage on Windows`)
  +  `docker run game-client`
  
## Known issues

+ No tests.
+ Many things are hard-coded, such as the amount of integer & fraction digits stored in the database, background process execution interval, number of transactions to take into consideration, also random operations & request sending interval in the client.
+ No authentication & authorization.
+ Transactions are not stored in the database.
+ No hook to back up player state from memory into the database on server shutdown. 
  
