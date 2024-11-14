package com.auction.service;

import com.auction.model.Item;
import com.auction.model.User;
import com.auction.model.Bid;
import com.auction.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Auction {

    // Method for Buyer to view available auction items
    public void viewItemsForSale() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Items WHERE status = 'active'";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                System.out.println("Items available for Auction:");
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double currentPrice = rs.getDouble("current_price");
                    Timestamp auctionEndTime = rs.getTimestamp("auction_end_time");
                    System.out.printf("Item ID: %d, Name: %s, Description: %s, Current Price: %.2f, Auction End: %s%n",
                            itemId, name, description, currentPrice, auctionEndTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for placing a bid by the Buyer
    public void placeBid(Item item, User user, double bidAmount) throws SQLException {
        if (bidAmount > item.getCurrentPrice()) {
            item.setCurrentPrice(bidAmount);
            Bid bid = new Bid(0, item.getItemId(), user.getUserId(), bidAmount, new Timestamp(System.currentTimeMillis()));
            System.out.println("Bid placed successfully.");

            // Insert bid into the database
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO Bids (item_id, user_id, bid_amount, bid_time) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, item.getItemId());
                    stmt.setInt(2, user.getUserId());
                    stmt.setDouble(3, bidAmount);
                    stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    stmt.executeUpdate();
                }
            }
        } else {
            System.out.println("Bid amount must be higher than the current bid.");
        }
    }

    // Admin can view auction history (all items and their bids)
    public void viewAuctionHistory() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT i.item_id, i.name, i.description, i.starting_price, i.current_price, i.auction_end_time, b.bid_id, b.bid_amount, u.name AS bidder_name "
                    + "FROM Items i LEFT JOIN Bids b ON i.item_id = b.item_id "
                    + "LEFT JOIN Users u ON b.user_id = u.user_id "
                    + "ORDER BY i.item_id, b.bid_time DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                System.out.println("Auction History:");
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    String itemName = rs.getString("name");
                    String description = rs.getString("description");
                    double startingPrice = rs.getDouble("starting_price");
                    double currentPrice = rs.getDouble("current_price");
                    Timestamp auctionEndTime = rs.getTimestamp("auction_end_time");
                    int bidId = rs.getInt("bid_id");
                    double bidAmount = rs.getDouble("bid_amount");
                    String bidderName = rs.getString("bidder_name");

                    System.out.printf("Item ID: %d, Name: %s, Description: %s, Starting Price: %.2f, Current Price: %.2f, Auction End Time: %s%n",
                            itemId, itemName, description, startingPrice, currentPrice, auctionEndTime);
                    if (bidId != 0) {
                        System.out.printf("\tBid ID: %d, Bidder: %s, Bid Amount: %.2f%n", bidId, bidderName, bidAmount);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to insert item as seller
    public void addItem(Item item) {
        try (Connection conn = DBConnection.getConnection()) {
            // Ensure your SQL query matches the structure of the Items table
            String sql = "INSERT INTO Items (seller_id, name, description, starting_price, current_price, auction_end_time, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Correctly setting the parameters for the SQL query
                stmt.setInt(1, item.getSellerId());  // seller_id
                stmt.setString(2, item.getName());    // name
                stmt.setString(3, item.getDescription()); // description
                stmt.setDouble(4, item.getStartingPrice()); // starting_price
                stmt.setDouble(5, item.getCurrentPrice());  // current_price (should be equal to starting price initially)
                stmt.setTimestamp(6, item.getAuctionEndTime());  // auction_end_time (Timestamp)
                stmt.setString(7, item.getStatus());  // status
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Item added successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
