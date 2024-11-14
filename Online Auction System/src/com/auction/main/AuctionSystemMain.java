package com.auction.main;
import com.auction.model.User;
import com.auction.DBConnection;
import com.auction.model.Item;
import com.auction.service.Auction;

import java.sql.*;
import java.util.Scanner;

public class AuctionSystemMain {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Auction auctionService = new Auction();

        boolean running = true;

        while (running) {
            System.out.println("Welcome to the Online Auction System");
            System.out.println("1. Register as Seller");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Register as a seller
                    System.out.println("Enter name:");
                    String name = scanner.next();
                    System.out.println("Enter email:");
                    String email = scanner.next();
                    System.out.println("Enter password:");
                    String password = scanner.next();  
                    User seller = new User(0, name, email, "seller", password);  
                    System.out.println("Seller registered successfully.");
                    
                    try (Connection conn = DBConnection.getConnection()) {
                        String sql = "INSERT INTO Users (name, email, role, password) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setString(1, seller.getName());
                            stmt.setString(2, seller.getEmail());
                            stmt.setString(3, seller.getRole());
                            stmt.setString(4, seller.getPassword());  
                            stmt.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.println("Enter user ID:");
                    int userId = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.println("Enter role (seller/buyer/admin):");
                    String role = scanner.nextLine();

                    User user = new User(userId, "Test User", "test@user.com", role, "password123");

                   
                    if ("seller".equalsIgnoreCase(role)) {
                        System.out.println("Login successful as seller.");

                        System.out.println("Enter item name:");
                        String itemName = scanner.nextLine();
                        System.out.println("Enter item description:");
                        String itemDescription = scanner.nextLine();
                        System.out.println("Enter starting price:");
                        while (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a valid starting price:");
                            scanner.next(); 
                        }
                        double startingPrice = scanner.nextDouble();
                        scanner.nextLine();  
                        System.out.println("Enter auction end time (yyyy-mm-dd hh:mm:ss):");
                        String auctionEndTimeStr = scanner.nextLine();
                        Timestamp auctionEndTime = Timestamp.valueOf(auctionEndTimeStr);

                        Item item = new Item(0, userId, itemName, itemDescription, startingPrice, startingPrice, auctionEndTime, "active");
                        auctionService.addItem(item);

                    } else if ("buyer".equalsIgnoreCase(role)) {
                        System.out.println("Login successful as buyer.");

                        auctionService.viewItemsForSale();
                        System.out.println("Enter item ID to bid on:");
                        int itemIdToBidOn = scanner.nextInt();
                        System.out.println("Enter your bid amount:");
                        while (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a valid bid amount:");
                            scanner.next(); 
                        }
                        double bidAmount = scanner.nextDouble();

                        User buyer = new User(1, "Buyer", "buyer@example.com", "buyer", "buy123");

                        Item itemToBidOn = new Item(itemIdToBidOn, 0, "Item", "Description", 0.0, 0.0, new Timestamp(System.currentTimeMillis()), "active");  // This would be fetched from DB
                        auctionService.placeBid(itemToBidOn, buyer, bidAmount);

                    } else if ("admin".equalsIgnoreCase(role)) {
                        System.out.println("Login successful as admin.");

                        auctionService.viewAuctionHistory();
                    } else {
                        System.out.println("Invalid role entered.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}
