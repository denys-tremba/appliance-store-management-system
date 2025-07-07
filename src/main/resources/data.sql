-- Updated data.sql

INSERT INTO manufacturers (id, name)
VALUES (DEFAULT, 'Samsung Electronics'),
       (DEFAULT, 'LG Corporation'),
       (DEFAULT, 'Sony Group Corporation'),
       (DEFAULT, 'Panasonic Corporation'),
       (DEFAULT, 'Whirlpool Corporation'),
       (DEFAULT, 'Bosch Home Appliances'),
       (DEFAULT, 'Apple Inc.'),
       (DEFAULT, 'Dell Technologies'),
       (9, 'Dyson'),
       (10, 'Ninja'),
       (11, 'KitchenAid'),
       (12, 'Philips'),
       (13, 'Breville'),
       (14, 'Anker'),
       (15, 'Nintendo'),
       (16, 'Sonos'),
       (17, 'Google'),
       (18, 'Vitamix'),
       (19, 'Frigidaire'),
       (20, 'GE Appliances'),
       (21, 'JennAir'),
       (22, 'Braun');

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

-- Appliances with new, realistic categories
INSERT INTO appliances (id, name, model, category, characteristic, description, power, power_type, price, manufacturer_id, created_at, version)
VALUES
    (DEFAULT, 'Smart Refrigerator', 'RF28R7351SR', 'MAJOR_APPLIANCES', '28 cu. ft., Family Hub, Wi-Fi', 'A spacious smart refrigerator with a touchscreen display and internal cameras.', 150, 'AC220', 2499.99, 1, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'OLED 4K TV', 'OLED65C1PUB', 'AUDIO_VIDEO', '65-inch, ThinQ AI, HDR', 'Stunning picture quality with self-lit pixels and smart features.', 120, 'AC220', 1899.00, 2, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Wireless Headphones', 'WH-1000XM5', 'AUDIO_VIDEO', 'Noise Cancelling, 30hr battery', 'Industry-leading noise cancelling headphones with exceptional sound.', 5, 'ACCUMULATOR', 348.00, 3, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Microwave Oven', 'NN-SN966S', 'KITCHEN_COUNTERTOP', '2.2 cu. ft., Inverter Technology', 'Powerful microwave with even cooking technology.', 1250, 'AC110', 299.50, 4, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Front Load Washer', 'WFW5620HW', 'MAJOR_APPLIANCES', '4.5 cu. ft., Steam Clean', 'High-efficiency front load washer with multiple wash cycles.', 800, 'AC220', 799.00, 5, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Dishwasher', 'SHPM65Z55N', 'MAJOR_APPLIANCES', 'Quiet, 3rd Rack, Stainless Steel', 'Super quiet dishwasher with flexible loading options.', 1300, 'AC220', 849.00, 6, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Smartphone', 'iPhone 15 Pro', 'COMPUTING_MOBILE', 'A17 Bionic, Pro Camera System', 'The latest flagship smartphone with advanced features.', 15, 'ACCUMULATOR', 999.00, 7, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Laptop', 'XPS 15', 'COMPUTING_MOBILE', 'Intel Core i7, 16GB RAM, 512GB SSD', 'Powerful and portable laptop for professionals.', 65, 'ACCUMULATOR', 1499.99, 8, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Air Purifier', 'Pure Cool TP07', 'HOME_COMFORT', 'HEPA Filter, Wi-Fi, Fan', 'Purifies and cools the air in your room.', 40, 'AC110', 549.00, 9, CURRENT_TIMESTAMP, 0), -- Dyson
    (DEFAULT, 'Robot Vacuum', 'Roomba s9+', 'CLEANING_FLOORCARE', 'Self-Emptying, Smart Mapping', 'Advanced robot vacuum with automatic dirt disposal.', 30, 'ACCUMULATOR', 799.00, 5, CURRENT_TIMESTAMP, 0), -- iRobot/Whirlpool
    (DEFAULT, 'Artisan Stand Mixer', 'KSM150PSER', 'KITCHEN_COUNTERTOP', '5-Quart, 10-Speed, Tilt-Head', 'A culinary icon. This durable, all-metal mixer provides power for mixing dough and whipping cream. With over 10 attachments, it transforms your kitchen into a hub for pasta making, grinding meat, or churning ice cream.', 325, 'AC110', 449.99, 11, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Barista Express Machine', 'BES870XL', 'KITCHEN_COUNTERTOP', 'Integrated Grinder, Steam Wand', 'Go from bean to espresso in under a minute. This all-in-one machine with a built-in conical burr grinder and precise temperature control allows you to craft authentic, café-quality espresso, lattes, and cappuccinos at home.', 1600, 'AC110', 749.95, 13, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Premium Air Fryer', 'AF161', 'KITCHEN_COUNTERTOP', '5.5-Quart, Max Crisp Technology', 'Enjoy guilt-free fried food. This powerful air fryer circulates super-hot air to cook your favorite meals with up to 75% less fat. Its large capacity basket is perfect for family-sized portions of crispy fries and more.', 1750, 'AC110', 129.99, 10, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Professional Blender', '5200', 'KITCHEN_COUNTERTOP', '64-oz Container, Variable Speed', 'Effortlessly blend tough ingredients from frozen fruit to fibrous greens. Create silky smoothies, hot soups, and frozen desserts with a powerful motor that ensures smooth results every time.', 1380, 'AC110', 449.95, 18, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Smart Oven Air Fryer Pro', 'BOV900BSS', 'KITCHEN_COUNTERTOP', 'Convection, 1 cu. ft. capacity', 'A versatile countertop powerhouse. This smart oven does it all: air fry, dehydrate, roast, and proof. Its Element iQ system delivers precise cooking temperatures, ensuring perfectly cooked meals with an intuitive user interface.', 1800, 'AC110', 399.95, 13, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Cordless Stick Vacuum', 'V15 Detect', 'CLEANING_FLOORCARE', 'Laser Illumination, 60min Runtime', 'Revolutionizes deep cleaning. A precisely angled laser reveals microscopic dust on hard floors, while a sensor sizes and counts dust particles, automatically adapting suction power for a scientifically proven deep clean.', 150, 'ACCUMULATOR', 749.99, 9, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Smart Thermostat', 'T3007ES', 'HOME_COMFORT', 'Learns Schedule, Remote Control', 'An intelligent thermostat that saves energy by learning your habits and programming itself. Control the temperature from anywhere with your phone, and get monthly energy reports to track your savings.', 5, 'AC220', 249.00, 17, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Window Air Conditioner', 'LW1017ERSM', 'HOME_COMFORT', '10,000 BTU, Wi-Fi Control', 'Beat the heat with this powerful and quiet window AC. Cools rooms up to 450 sq. ft. and can be controlled from your smartphone via the ThinQ app. Its multiple fan speeds and energy-saver mode keep you comfortable.', 900, 'AC110', 459.00, 2, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Dehumidifier with Pump', 'FFAP5033W1', 'HOME_COMFORT', '50-Pint, Continuous Drain', 'Protect your home from mold and mildew. This unit efficiently removes 50 pints of moisture per day and features a continuous drain option with a built-in pump, so you never have to empty a bucket again.', 720, 'AC110', 299.00, 19, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Arc Smart Soundbar', 'ARC-G1-US1', 'AUDIO_VIDEO', 'Dolby Atmos, Voice Control, Wi-Fi', 'Bring all your entertainment to life with the premium smart soundbar for TV, movies, and music. Features breathtaking, cinematic Dolby Atmos sound and built-in voice control with Amazon Alexa and Google Assistant.', 76, 'AC110', 899.00, 16, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Gaming Console', 'OLED Model', 'GAMING_CONSOLES', '7-inch OLED Screen, 64GB Storage', 'Experience gaming on the go or at home with a vibrant 7-inch OLED screen that makes colors pop. This versatile console transitions from a home system to a portable handheld, offering a vast library of games.', 18, 'ACCUMULATOR', 349.99, 15, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Alpha Mirrorless Camera', 'a7 IV', 'AUDIO_VIDEO', '33MP Full-Frame, 4K 60p Video', 'A new benchmark in full-frame imaging. The Alpha 7 IV blends outstanding image quality with advanced video features and relentless autofocus performance. The ideal hybrid camera for modern creators.', 10, 'ACCUMULATOR', 2499.99, 3, CURRENT_TIMESTAMP, 0),
    (DEFAULT, '4K Streaming Player', 'GZRNL', 'AUDIO_VIDEO', '4K HDR, Dolby Vision, Voice Remote', 'Upgrade your TV experience with brilliant 4K HDR picture quality and access to thousands of channels. The simple remote with voice search makes it easy to find your favorite movies and shows across all your apps.', 4, 'AC110', 99.99, 17, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Portable Projector', 'Nebula Capsule II', 'AUDIO_VIDEO', '720p, 200 ANSI Lumens, Android TV', 'Enjoy a cinematic experience anywhere. This soda-can-sized projector delivers a remarkably bright and clear picture up to 100 inches. With built-in Android TV, you can access thousands of apps directly from the device.', 45, 'ACCUMULATOR', 579.99, 14, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Smart Gas Dryer', 'DLEX4200B', 'MAJOR_APPLIANCES', '7.4 cu. ft., TurboSteam, AI Sensor', 'A dryer that does the thinking for you. AI technology selects the optimal drying settings, while TurboSteam technology refreshes clothes and reduces wrinkles in just 10 minutes. Wi-Fi lets you monitor cycles remotely.', 650, 'AC110', 1099.00, 2, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'UltraFresh Washer', 'GFW850SPNRS', 'MAJOR_APPLIANCES', '5.0 cu. ft., SmartDispense', 'The first washer designed to stay fresh and clean. The UltraFresh Vent system with OdorBlock eliminates excess moisture to prevent odors, ensuring your washer and clothes stay pristine.', 950, 'AC220', 1149.00, 20, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'French Door Fridge', 'WRF535SWHZ', 'MAJOR_APPLIANCES', '25 cu. ft., Fingerprint Resistant', 'Store all your family favorites with ease. This spacious French door refrigerator features wall-to-wall frameless glass shelves for flexible storage. The exterior ice and water dispenser offers filtered hydration.', 200, 'AC110', 1699.00, 5, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Gas Cooktop', 'JCG7530SLSS', 'MAJOR_APPLIANCES', '30-inch, 5 Burners, 18K BTU', 'Achieve professional cooking results. This cooktop features five sealed burners, including a powerful 18,000 BTU burner for rapid boiling and a low-simmer burner for delicate sauces. Easy to clean and stylish.', 5, 'AC110', 1399.00, 21, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Electric Toothbrush', 'DiamondClean 9700', 'PERSONAL_CARE', '5 Modes, 3 Intensities, Sensor', 'The ultimate in oral care. Smart sensors and an app provide real-time feedback on your brushing, ensuring complete coverage and perfect pressure. It removes up to 10x more plaque for healthier gums in two weeks.', 2, 'ACCUMULATOR', 279.96, 12, CURRENT_TIMESTAMP, 0),
    (DEFAULT, 'Electric Shaver', 'Series 9 Pro', 'PERSONAL_CARE', 'Wet & Dry, 4+1 Shaving Head', 'Experience our most efficient and comfortable shave. The innovative ProHead with 5 shaving elements captures more hair in one stroke, even on dense beards. The included Clean&Charge station hygienically cleans it.', 7, 'ACCUMULATOR', 329.99, 22, CURRENT_TIMESTAMP, 0);

-- Orders
INSERT INTO orders (id, client_id, employee_id, state, created_at)
VALUES (DEFAULT, 1, 6, 'APPROVED', CURRENT_TIMESTAMP),
       (DEFAULT, 2, 7, 'WAITING_FOR_APPROVAL', CURRENT_TIMESTAMP),
       (DEFAULT, 1, 6, 'WAITING_FOR_COMPLETION', CURRENT_TIMESTAMP),
       (DEFAULT, 3, 8, 'DISAPPROVED', CURRENT_TIMESTAMP),
       (DEFAULT, 4, 7, 'WAITING_FOR_COMPLETION', CURRENT_TIMESTAMP),
       (DEFAULT, 5, 7, 'WAITING_FOR_APPROVAL', CURRENT_TIMESTAMP),
       (DEFAULT, 2, 8, 'APPROVED', CURRENT_TIMESTAMP);

-- Order Line Items
INSERT INTO order_line_items (order_id, appliance_id, quantity, price)
VALUES (1, 2, 1, 1899.00), (1, 3, 2, 696.00),
       (2, 7, 1, 999.00), (2, 8, 1, 1499.99),
       (3, 5, 1, 799.00),
       (4, 1, 1, 2499.99), (4, 6, 1, 849.00), (4, 9, 1, 549.00),
       (5, 4, 2, 599.00), (5, 10, 1, 799.00),
       (6, 3, 1, 348.00),
       (7, 7, 2, 1998.00);