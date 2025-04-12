-- Insérer des données dans la table animals
INSERT INTO animals (titre, description, image_url) VALUES
('Chien Labrador', 'Un adorable chien Labrador à vendre.', 'https://example.com/images/labrador.jpg'),
('Chat Persan', 'Un magnifique chat Persan à vendre.', 'https://example.com/images/persan.jpg'),
('Lapin Nain', 'Un petit lapin nain à vendre.', 'https://example.com/images/lapin.jpg'),
('Chien Berger Allemand', 'Un chien Berger Allemand très intelligent et loyal.', 'https://example.com/images/berger_allemand.jpg'),
('Chat Bengal', 'Un chat Bengal avec un pelage magnifique.', 'https://example.com/images/bengal.jpg'),
('Chien Golden Retriever', 'Un chien Golden Retriever affectueux et joueur.', 'https://example.com/images/golden_retriever.jpg');

-- Insérer des données dans la table users
INSERT INTO users (name, email, password) VALUES
('Alice', 'alice@example.com', 'password123'),
('Bob', 'bob@example.com', 'password456'),
('Charlie', 'charlie@example.com', 'password789');

-- Insérer des données dans la table contact_messages
INSERT INTO contact_messages (name, email, message) VALUES
('Alice', 'alice@example.com', 'Je suis intéressée par le chien Labrador.'),
('Bob', 'bob@example.com', 'Je voudrais en savoir plus sur le chat Persan.'),
('Charlie', 'charlie@example.com', 'Est-ce que le lapin nain est toujours disponible ?');
