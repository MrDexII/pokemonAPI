db.createUser(
        {
            user: "pokemonUser",
            pwd: "password",
            roles: [
                {
                    role: "readWrite",
                    db: "pokemon_db"
                }
            ]
        }
);