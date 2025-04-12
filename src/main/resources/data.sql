-- Insertion de données dans la table users
INSERT INTO users (email, password, name, role, created_at) VALUES
('buyer1@example.com', 'password123', 'Alice Buyer', 'BUYER', CURRENT_TIMESTAMP),
('seller1@example.com', 'password123', 'Bob Seller', 'SELLER', CURRENT_TIMESTAMP),
('buyer2@example.com', 'password123', 'Charlie Buyer', 'BUYER', CURRENT_TIMESTAMP),
('seller2@example.com', 'password123', 'Dana Seller', 'SELLER', CURRENT_TIMESTAMP),
('buyer3@example.com', 'password123', 'Eve Buyer', 'BUYER', CURRENT_TIMESTAMP),
('seller3@example.com', 'password123', 'Frank Seller', 'SELLER', CURRENT_TIMESTAMP),
('buyer4@example.com', 'password123', 'Grace Buyer', 'BUYER', CURRENT_TIMESTAMP),
('seller4@example.com', 'password123', 'Hank Seller', 'SELLER', CURRENT_TIMESTAMP),
('buyer5@example.com', 'password123', 'Ivy Buyer', 'BUYER', CURRENT_TIMESTAMP),
('seller5@example.com', 'password123', 'Jack Seller', 'SELLER', CURRENT_TIMESTAMP);

-- Insertion de données dans la table animal
INSERT INTO animal (species, breed, age, sex) VALUES
('Dog', 'Labrador', 3, 'MALE'),
('Cat', 'Siamese', 2, 'FEMALE'),
('Bird', 'Parrot', 1, 'MALE'),
('Rabbit', 'Angora', 4, 'FEMALE'),
('Dog', 'Beagle', 5, 'MALE'),
('Cat', 'Persian', 3, 'FEMALE'),
('Bird', 'Canary', 2, 'MALE'),
('Rabbit', 'Dutch', 1, 'FEMALE'),
('Dog', 'Bulldog', 4, 'MALE'),
('Cat', 'Maine Coon', 2, 'FEMALE');

-- Insertion de données dans la table ad
INSERT INTO ad (title, price, location, created_at, animal_id, seller_id) VALUES
('Adorable Labrador for Sale', 500.00, 'Paris', CURRENT_TIMESTAMP, 1, 2),
('Siamese Cat Looking for a Home', 300.00, 'Lyon', CURRENT_TIMESTAMP, 2, 4),
('Friendly Parrot Available', 150.00, 'Marseille', CURRENT_TIMESTAMP, 3, 2),
('Cute Angora Rabbit', 100.00, 'Nice', CURRENT_TIMESTAMP, 4, 4),
('Friendly Beagle for Sale', 450.00, 'Bordeaux', CURRENT_TIMESTAMP, 5, 6),
('Beautiful Persian Cat', 350.00, 'Toulouse', CURRENT_TIMESTAMP, 6, 8),
('Singing Canary', 100.00, 'Lille', CURRENT_TIMESTAMP, 7, 6),
('Cute Dutch Rabbit', 80.00, 'Strasbourg', CURRENT_TIMESTAMP, 8, 8),
('Adorable Bulldog', 600.00, 'Nantes', CURRENT_TIMESTAMP, 9, 10),
('Majestic Maine Coon', 400.00, 'Rennes', CURRENT_TIMESTAMP, 10, 8);

-- Insertion de données dans la table photo
INSERT INTO photo (url, uploaded_at, ad_id) VALUES
('/images/chat.JPEG', CURRENT_TIMESTAMP, 1),
('/images/chien.JPEG', CURRENT_TIMESTAMP, 2),
('/images/boeuf.JPEG', CURRENT_TIMESTAMP, 3),
('/images/dromadaire.JPEG', CURRENT_TIMESTAMP, 4),
('/images/chameau.JPEG', CURRENT_TIMESTAMP, 5),
('/images/chevre.JPEG', CURRENT_TIMESTAMP, 6),
('/images/cheval.JPEG', CURRENT_TIMESTAMP, 7),
('/images/lapin.JPEG', CURRENT_TIMESTAMP, 8),
('/images/ane/JPEG', CURRENT_TIMESTAMP, 9),
('/images/mouton.JPEG', CURRENT_TIMESTAMP, 10);

-- Insertion de données dans la table contact_request
INSERT INTO contact_request (ad_id, sender_id, content, sent_at) VALUES
(1, 1, 'I am interested in the Labrador.', CURRENT_TIMESTAMP),
(2, 3, 'Can I see the Siamese cat?', CURRENT_TIMESTAMP),
(3, 1, 'Is the parrot still available?', CURRENT_TIMESTAMP),
(4, 3, 'I would like to buy the rabbit.', CURRENT_TIMESTAMP),
(5, 3, 'Interested in the Beagle.', CURRENT_TIMESTAMP),
(6, 5, 'Can I see the Persian cat?', CURRENT_TIMESTAMP),
(7, 3, 'Is the Canary still available?', CURRENT_TIMESTAMP),
(8, 5, 'I would like to buy the Dutch rabbit.', CURRENT_TIMESTAMP),
(9, 3, 'Interested in the Bulldog.', CURRENT_TIMESTAMP),
(10, 5, 'Can I see the Maine Coon?', CURRENT_TIMESTAMP);
