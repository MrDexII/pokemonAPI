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
