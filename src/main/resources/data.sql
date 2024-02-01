INSERT INTO roles(role_id, role)
VALUES (1, 'ADMIN'),
       (2, 'USER');

--admin user
INSERT INTO users(user_id, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username)
VALUES(1,1,1,1,1,'$2a$10$PhtB2sSCLGHRdDVJ/6/vrunFNz2p5oLy7Khr87FrgzQudikvPrgE.','admin');

INSERT INTO users_roles(user_id, role_id)
VALUES (1,1),(1,2);

--user
INSERT INTO users(user_id, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username)
VALUES (2,1,1,1,1,'$2a$10$0ACmaDjVFsfKDyxr2gsi4ODiL.M0LDqgqvbNbijOn47A4hizY0pNe','user');

INSERT INTO users_roles(user_id, role_id)
VALUES (2,2);