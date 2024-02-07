# PokemonAPI
## Wykorzystane technologie, narzędzia 
**IntelliJ IDEA, Maven, Java, Spring Boot , MySQL, MongoDB, JWT, HATEOAS, Hibernate**
## Opis Aplikacji
API pozwala na podstawowe operacje tworzenia, odczytywania, uaktualnienia, kasowania (CRUD)
pokemonów i ich typów  w bazie danych MongoDB. Aplikacja wykorzystuje mechanizm autoryzacji poprzez Bearer token (JWT),
wiec aby korzystać z API należy posiadać konto. Użytkownicy API są przechowywani w oddzielnej bazie MySQL. Zwykli użytkownicy 
mogą tylko odczytywać dane z bazy danych, do reszty funkcjonalności mają dostęp tyko konta administratorów. Na potrzeb testowania
podczas startu aplikacja tworzy dwa konta jedno dla administratora

```
Login: admin

Hasło: admin
```

A także konto użytkownika

```
Login: user

Hasło: user
```
## Endpointy

### Swagger 
http://localhost:8080/swagger-ui/ - gui do przeglądania endpointów 

### Login
POST http://localhost:8080/login - logowanie 

### Elasticsearch
GET http://localhost:8080/elastic/{pokemonName} - szukaj kiedy piszesz 

### WebSocket
GET http://localhost:8080/gameSession/{id} - szuka sesji gry o podanym id

### Pokemon
POST http://localhost:8080/pokemon/ - dodaje nowego pokemona 

GET http://localhost:8080/pokemon/page?page={pageNumber}&size={sizeNumber} - zwraca listę pokemonów z wykorzystaniem stronicowania

GET http://localhost:8080/pokemon/{id} - zwraca pokemona o podanym id 

GET http://localhost:8080/pokemon/find?name={name} - zwraca pokemona o podanej nazwie

PUT http://localhost:8080/pokemon/{id} - uaktualnia pokemona o danym id 

DELETE http://localhost:8080/pokemon/{id} - usuwa pokemona o danym id 

GET http://localhost:8080/pokemon/findByPokemonNumber?number={number} - wyszukuje pokemona po numerze w pokedeksie 

### PokemonType
POST http://localhost:8080/pokemon/type/ - dodaje nowy typ pokemona

GET http://localhost:8080/pokemon/type/ - zwraca listę typów pokemona

GET http://localhost:8080/pokemon/type/{id} - zwraca typ pokemona o podanym id

GET http://localhost:8080/pokemon/type/find?name={name} - zwraca typ pokemona o podanej nazwie

PUT http://localhost:8080/pokemon/type/{id} - uaktualnia typ pokemona o danym id

DELETE http://localhost:8080/pokemon/type/{id} - usuwa pokemona o danym id

### Roles

GET http://localhost:8080/user/role/ - zwraca wszystkich typy użytkowników

### Users

POST http://localhost:8080/pokemon/user/ - dodaje użytkownika

GET http://localhost:8080/pokemon/user/ - zwraca listę użytkowników

GET http://localhost:8080/pokemon/user/{id} - zwraca użytkownika o podanym id

GET http://localhost:8080/pokemon/user/find?name={name} - zwraca użytkownika o podanej nazwie użytkownika

PUT http://localhost:8080/pokemon/user/{id} - uaktualnia użytkownika o danym id

DELETE http://localhost:8080/pokemon/user/{id} - usuwa użytkownika o danym id

### Rabbitmq topics
- /topic/gameChat
- /topic/users-user{userSessionId}
- /topic/lobby.{lobbyId}
