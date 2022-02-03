CREATE TABLE IF NOT EXISTS role (
    role_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    is_account_non_expired BIT NOT NULL,
    is_account_non_locked BIT NOT NULL,
    is_credentials_non_expired Bit NOT NULL,
    is_enabled BIT NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (role_id) REFERENCES role (role_id)
);
