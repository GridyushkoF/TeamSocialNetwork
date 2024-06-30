insert into "users" (id, reg_date, email, first_name, last_name, password, role)
    values (1, '2024-06-30 12:06:47.297523', 'admin@socialnetwork.com', 'Firstname', 'Lastname', '$2a$10$56fEHY86Cg3KZdBG4LIFV.bfv2Mhox0ptayasvG5zQgo15fQde.9G', 'ADMIN');
ALTER SEQUENCE users_id_seq RESTART WITH 2;
