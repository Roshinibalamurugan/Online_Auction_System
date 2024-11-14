CREATE DATABASE auction;
USE auction;
CREATE TABLE Users (user_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, email VARCHAR(255) UNIQUE NOT NULL,
password VARCHAR(255) NOT NULL, role ENUM('buyer', 'seller', 'admin') NOT NULL,  status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP);

CREATE TABLE Items ( item_id INT PRIMARY KEY AUTO_INCREMENT, seller_id INT NOT NULL, name VARCHAR(255) NOT NULL, description TEXT,
starting_price DECIMAL(10, 2) NOT NULL, current_price DECIMAL(10, 2) DEFAULT 0.00,  auction_end_time TIMESTAMP NOT NULL,
status ENUM('active', 'closed') DEFAULT 'active', created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (seller_id) REFERENCES Users(user_id));

CREATE TABLE Bids ( bid_id INT PRIMARY KEY AUTO_INCREMENT, item_id INT NOT NULL, user_id INT NOT NULL, bid_amount DECIMAL(10, 2) NOT NULL,
bid_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, is_auto_bid BOOLEAN DEFAULT FALSE, FOREIGN KEY (item_id) REFERENCES Items(item_id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE);

CREATE TABLE AuctionHistory (auction_history_id INT PRIMARY KEY AUTO_INCREMENT,item_id INT NOT NULL,
winner_id INT, final_price DECIMAL(10, 2) NOT NULL, closed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (item_id) REFERENCES Items(item_id) ON DELETE CASCADE, FOREIGN KEY (winner_id) REFERENCES Users(user_id) ON DELETE SET NULL);

CREATE TABLE Notifications (notification_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL,
message TEXT NOT NULL, status ENUM('unread', 'read') DEFAULT 'unread', sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE);

CREATE TABLE SellerApplications (application_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL,
submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
 approved_at TIMESTAMP, FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE);

CREATE TABLE Reports (
    report_id INT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT,
    report_type VARCHAR(100) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data TEXT NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES Users(user_id) ON DELETE SET NULL
);

INSERT INTO Users (name, email, password, role, status) VALUES
('Latha', 'latha@example.com', 'latha@11', 'seller', 'approved'),
('Jeno', 'jeno@example.com', 'jenoprase', 'buyer', 'approved'),
('Priya', 'priya@example.com', 'priyaswe', 'seller', 'approved'),
('Megala', 'megala@example.com', 'mega', 'buyer', 'approved'),
('Sanjeevi', 'sanjeevi@example.com', 'sanj123', 'admin', 'approved'),
('Pavi', 'pavi@example.com', 'paviAj', 'buyer', 'approved'),
('Pabi', 'pabi@example.com', 'pabi@123', 'seller', 'approved');

INSERT INTO Items (seller_id, name, description, starting_price, current_price, auction_end_time, status) VALUES
(1, 'Watch', 'A beautiful vintage watch.', 150.00, 250.00, '2024-12-01 15:00:00', 'active'),
(2, 'Furniture', 'A rare teak furniture', 100.00, 200.00, '2024-12-05 12:00:00', 'active'),
(3, 'Necklace', 'A designed handmade necklace.', 700.00, 1000.00, '2024-12-03 18:00:00', 'active'),
(4, 'Painting', 'A vintage painting from the 18th century.', 500.00, 800.00, '2024-12-10 20:00:00', 'active'),
(5, 'Sculpture', 'A handcrafted wooden sculpture.', 150.00, 350.00, '2024-12-07 17:30:00', 'active');

INSERT INTO Bids (item_id, user_id, bid_amount, is_auto_bid) VALUES
(1, 1, 150.00, FALSE),
(1, 2, 170.00, TRUE),
(2, 3, 210.00, FALSE),
(3, 4, 55.00, FALSE),
(4, 5, 125.00, TRUE);

INSERT INTO AuctionHistory (item_id, winner_id, final_price, closed_at) VALUES
(1, 2, 170.00, '2024-12-01 15:05:00'),
(2, 3, 210.00, '2024-12-05 12:30:00'),
(3, 4, 55.00, '2024-12-03 18:15:00'),
(4, 5, 125.00, '2024-12-02 14:30:00'),
(5, 1, 300.00, '2024-12-08 19:30:00');

INSERT INTO Notifications (user_id, message, status) VALUES
(1, 'You have been outbid on the item Vintage Watch.', 'unread'),
(2, 'Your item Antique Vase has received a new bid.', 'unread'),
(3, 'You are the winning bidder for the Handmade Necklace.', 'read'),
(4, 'Auction for Old Book Collection has closed. You are the winner!', 'unread'),
(5, 'You have been outbid on Leather Jacket.', 'read');

INSERT INTO SellerApplications (user_id, status, approved_at) VALUES
(1, 'approved', '2024-11-23 11:45:00'),(2, 'approved', '2024-11-20 10:00:00'),
(3, 'approved', '2024-11-22 09:30:00'),
(4, 'pending', NULL),
(5, 'rejected', '2024-11-21 14:00:00');

INSERT INTO Reports (admin_id, report_type, generated_at, data) VALUES
(1, 'Auction Summary', '2024-11-01 12:00:00', 'Summary of all auctions closed in October'),
(2, 'User Activity Report', '2024-11-15 14:00:00', 'Detailed report on active users and bids placed'),
(3, 'Revenue Report', '2024-11-10 17:30:00', 'Monthly revenue breakdown for November'),
(4, 'New Sellers Report', '2024-11-20 10:30:00', 'Approved and rejected sellers overview'),
(5, 'Top Bidders Report', '2024-11-25 18:00:00', 'Top bidders and winning bids for November');

SELECT * FROM Users;

SELECT * FROM Items;

SELECT * FROM Bids;

SELECT * FROM AuctionHistory;

SELECT * FROM Notifications;

SELECT * FROM SellerApplications;

SELECT * FROM Reports;
