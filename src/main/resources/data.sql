REPLACE INTO role(role_id, role)
VALUES (1, 'ADMIN'),
       (2, 'USER');

--admin user
REPLACE INTO user(user_id, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username)
VALUES(1,1,1,1,1,'$2a$10$PhtB2sSCLGHRdDVJ/6/vrunFNz2p5oLy7Khr87FrgzQudikvPrgE.','admin');

REPLACE INTO user_role(user_id, role_id)
VALUES (1,1),(1,2);

--user
REPLACE INTO user(user_id, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username)
VALUES (2,1,1,1,1,'$2a$10$0ACmaDjVFsfKDyxr2gsi4ODiL.M0LDqgqvbNbijOn47A4hizY0pNe','user');

REPLACE INTO user_role(user_id, role_id)
VALUES (2,2);