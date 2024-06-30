insert into "users" (id, reg_date, email, first_name, last_name, password, role)
    values (1, '2024-06-30 12:06:47.297523', 'admin@socialnetwork.com', 'Firstname', 'Lastname', '$2a$10$jKwjnyGxN/oBixbpgyG7rug2QB/k6U7/35JyFPChXv6W5/QUvCa/u', 'ADMIN');
ALTER SEQUENCE users_id_seq RESTART WITH 2;
