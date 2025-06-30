INSERT INTO manufacturers (id, name)
VALUES (DEFAULT, 'Samsung Electronics'),
       (DEFAULT, 'LG Corporation'),
       (DEFAULT, 'Sony Group Corporation'),
       (DEFAULT, 'Panasonic Corporation'),
       (DEFAULT, 'Whirlpool Corporation'),
       (DEFAULT, 'Bosch Home Appliances'),
       (DEFAULT, 'Apple Inc.'),
       (DEFAULT, 'Dell Technologies');


-- Clients
INSERT INTO users (id, role, card, email, name, password)
VALUES (DEFAULT, 'client', '1111222233334444', 'alice.johnson@example.com', 'Alice Johnson', '{bcrypt}$2a$12$SXZtRBOLxfbyI1MUbrlMt.VvHaXRsFMAYLc/3PUSJcEL3yMSFLuSu'),
       (DEFAULT, 'client', '5555666677778888', 'bob.williams@example.com', 'Bob Williams', '{bcrypt}$2a$12$9RkpaVoq2wb0KSgCGjUJHeZivvhp0opQDRgmHMtOcEOSlV2QIEM.2'),
       (DEFAULT, 'client', '9999000011112222', 'carol.davis@example.com', 'Carol Davis', '{bcrypt}$2a$12$U2JqlWHJ3K/vCMQMbuDEBOWKSe7iiDsFCSdo5VFJDmdrv8uuLcAWu'),
       (DEFAULT, 'client', '3333444455556666', 'david.miller@example.com', 'David Miller', '{bcrypt}$2a$12$ZtMWN7oZd2xpEcUBhstK.OyVimEJ34D7g6kXs3rqsyuvstPe5386e'),
       (DEFAULT, 'client', '3333444455556667', 'eve.wilson@example.com', 'Eve Wilson', '{bcrypt}$2a$12$kGBn9OFGyLo.xzneAW3pzO06I3SV90Pt57CrYlf.K5eje0CWEgJXW');

-- Employees
INSERT INTO users (id, role, department, email, name, password)
VALUES (DEFAULT, 'employee', 'Sales', 'emily.clark@store.com', 'Emily Clark', '{bcrypt}$2a$12$SXZtRBOLxfbyI1MUbrlMt.VvHaXRsFMAYLc/3PUSJcEL3yMSFLuSu'),
       (DEFAULT, 'employee', 'Support', 'frank.lewis@store.com', 'Frank Lewis', '{bcrypt}$2a$12$9RkpaVoq2wb0KSgCGjUJHeZivvhp0opQDRgmHMtOcEOSlV2QIEM.2'),
       (DEFAULT, 'employee', 'Sales', 'grace.lee@store.com', 'Grace Lee', '{bcrypt}$2a$12$U2JqlWHJ3K/vCMQMbuDEBOWKSe7iiDsFCSdo5VFJDmdrv8uuLcAWu'),
       (DEFAULT, 'employee', 'Management', 'henry.walker@store.com', 'Henry Walker', '{bcrypt}$2a$12$ZtMWN7oZd2xpEcUBhstK.OyVimEJ34D7g6kXs3rqsyuvstPe5386e');

-- For Appliances, let's use Manufacturer IDs from 1 to 8
INSERT INTO appliances (id, name, model, category, characteristic, description, power, power_type, price,
                       manufacturer_id, created_at, version)
VALUES (DEFAULT, 'Smart Refrigerator', 'RF28R7351SR', 'BIG', '28 cu. ft., Family Hub, Wi-Fi',
        'A spacious smart refrigerator with a touchscreen display and internal cameras.', 150, 'AC220', 2499.99,
        1, CURRENT_TIMESTAMP, 0),                                                                                                   -- Samsung
       (DEFAULT, 'OLED 4K TV', 'OLED65C1PUB', 'BIG', '65-inch, ThinQ AI, HDR',
        'Stunning picture quality with self-lit pixels and smart features.', 120, 'AC220', 1899.00, 2, CURRENT_TIMESTAMP, 0),       -- LG
       (DEFAULT, 'Wireless Headphones', 'WH-1000XM5', 'SMALL', 'Noise Cancelling, 30hr battery',
        'Industry-leading noise cancelling headphones with exceptional sound.', 5, 'ACCUMULATOR', 348.00, 3, CURRENT_TIMESTAMP, 0), -- Sony
       (DEFAULT, 'Microwave Oven', 'NN-SN966S', 'BIG', '2.2 cu. ft., Inverter Technology',
        'Powerful microwave with even cooking technology.', 1250, 'AC110', 299.50, 4, CURRENT_TIMESTAMP, 0),                        -- Panasonic
       (DEFAULT, 'Front Load Washer', 'WFW5620HW', 'BIG', '4.5 cu. ft., Steam Clean',
        'High-efficiency front load washer with multiple wash cycles.', 800, 'AC220', 799.00, 5, CURRENT_TIMESTAMP, 0),             -- Whirlpool
       (DEFAULT, 'Dishwasher', 'SHPM65Z55N', 'BIG', 'Quiet, 3rd Rack, Stainless Steel',
        'Super quiet dishwasher with flexible loading options.', 1300, 'AC220', 849.00, 6, CURRENT_TIMESTAMP, 0),                   -- Bosch
       (DEFAULT, 'Smartphone', 'iPhone 15 Pro', 'SMALL', 'A17 Bionic, Pro Camera System',
        'The latest flagship smartphone with advanced features.', 280, 'ACCUMULATOR', 999.00, 7, CURRENT_TIMESTAMP, 0),                                                                                                   -- Apple (Power N/A or typically not listed like this)
       (DEFAULT, 'Laptop', 'XPS 15', 'SMALL', 'Intel Core i7, 16GB RAM, 512GB SSD',
        'Powerful and portable laptop for professionals.', 65, 'ACCUMULATOR', 1499.99, 8, CURRENT_TIMESTAMP, 0),                    -- Dell
       (DEFAULT, 'Air Purifier', 'Pure Cool TP07', 'SMALL', 'HEPA Filter, Wi-Fi, Fan',
        'Purifies and cools the air in your room.', 40, 'AC110', 549.00,
        1, CURRENT_TIMESTAMP, 0),                                                                                                   -- Samsung or another brand, let's say Samsung
       (DEFAULT, 'Robot Vacuum', 'Roomba s9+', 'SMALL', 'Self-Emptying, Smart Mapping',
        'Advanced robot vacuum with automatic dirt disposal.', 30, 'ACCUMULATOR', 799.00, 5, CURRENT_TIMESTAMP, 0);

INSERT INTO orders (id, client_id, employee_id, state, created_at)
VALUES (DEFAULT, 1, 6, 'APPROVED', CURRENT_TIMESTAMP),                 -- Alice's order, handled by Emily, committed
       (DEFAULT, 2, 7, 'WAITING_FOR_APPROVAL', CURRENT_TIMESTAMP),     -- Bob's order, handled by Frank, waiting approval
       (DEFAULT, 1, 6, 'WAITING_FOR_COMPLETION', CURRENT_TIMESTAMP),   -- Alice's second order, handled by Emily, new
       (DEFAULT, 3, 8, 'DISAPPROVED', CURRENT_TIMESTAMP),                 -- Carol's order, handled by Grace, committed
       (DEFAULT, 4, 7, 'WAITING_FOR_COMPLETION', CURRENT_TIMESTAMP),-- David's order, not yet assigned to an employee
       (DEFAULT, 5, 7, 'WAITING_FOR_APPROVAL', CURRENT_TIMESTAMP),     -- Eve's order, handled by Frank, waiting approval
       (DEFAULT, 2, 8, 'APPROVED', CURRENT_TIMESTAMP);

INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (1, 2, 1, 1899.00),
       (1, 3, 2, 696.00);

INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (2, 7, 1, 999.00),
       (2, 8, 1, 1499.99);

INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (3, 5, 1, 799.00);

INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (4, 1, 1, 2499.99),
       (4, 6, 1, 849.00),
       (4, 9, 1, 549.00);

INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (5, 4, 2, 599.00),
       (5, 10, 1, 799.00);

INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (6, 3, 1, 348.00);

INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (7, 7, 2, 1998.00); -- 2 iPhone 15 Pro