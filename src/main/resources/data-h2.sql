
insert into ecommerce.customers 
    (city, house_no, landmark, pin_code, street, contact_number, email, first_name, last_name, id) 
values 
    ('New York', '123', 'Central Park', '10001', 'Broadway', '+1234567890', 'john.doe@example.com', 'John', 'Doe', nextval('ecommerce.eid_generator_sequence'));


insert into ecommerce.customers 
    (city, house_no, landmark, pin_code, street, contact_number, email, first_name, last_name, id) 
values 
    ('Los Angeles', '456', 'Hollywood Blvd', '90001', 'Sunset Blvd', '+1987654321', 'jane.doe@example.com', 'Jane', 'Doe', nextval('ecommerce.eid_generator_sequence'));


insert into ecommerce.customers 
    (city, house_no, landmark, pin_code, street, contact_number, email, first_name, last_name, id) 
values 
    ('Chicago', '789', 'Millennium Park', '60601', 'Michigan Ave', '+1122334455', 'michael.smith@example.com', 'Michael', 'Smith', nextval('ecommerce.eid_generator_sequence'));


insert into ecommerce.customers 
    (city, house_no, landmark, pin_code, street, contact_number, email, first_name, last_name, id) 
values 
    ('San Francisco', '987', 'Golden Gate Bridge', '94102', 'Lombard St', '+1555666777', 'sarah.johnson@example.com', 'Sarah', 'Johnson', nextval('ecommerce.eid_generator_sequence'));


insert into ecommerce.customers 
    (city, house_no, landmark, pin_code, street, contact_number, email, first_name, last_name, id) 
values 
    ('London', '10', 'Hyde Park', 'W1J 7BX', 'Oxford St', '+447700900000', 'james.wilson@example.com', 'James', 'Wilson', nextval('ecommerce.eid_generator_sequence'));

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'mattress',
    2,
    900.99,
    '2024-03-14 14:20:00',
    165850
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'bedsheets',
    5,
    200.50,
    '2024-03-15 09:45:00',
    165851
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'blankets',
    3,
    100.25,
    '2024-03-16 12:10:00',
    165852
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'comforters',
    4,
    300.75,
    '2024-03-17 15:30:00',
    165853
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'pillows',
    6,
    400.50,
    '2024-03-18 18:45:00',
    165854
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'mattress',
    1,
    450.99,
    '2024-03-19 10:00:00',
    165852
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'bedsheets',
    8,
    300.25,
    '2024-03-20 12:30:00',
    165853
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'blankets',
    2,
    150.50,
    '2024-03-21 14:50:00',
    165854
);

-- Inserting into ecommerce.orders table
INSERT INTO ecommerce.orders (
    order_id, product_name, quantity, amount, order_time, customer_id
) VALUES (
    nextval('ecommerce.order_id_generator_sequence'), -- This will generate the next value from the sequence
    'comforters',
    7,
    500.75,
    '2024-03-22 17:15:00',
    165850
);
