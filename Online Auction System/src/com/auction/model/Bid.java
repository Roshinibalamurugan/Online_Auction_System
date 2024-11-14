package com.auction.model;

import java.sql.Timestamp;

public class Bid {
    private int bidId;
    private int itemId;
    private int userId;
    private double bidAmount;
    private Timestamp bidTime;

    public Bid(int bidId, int itemId, int userId, double bidAmount, Timestamp bidTime) {
        this.bidId = bidId;
        this.itemId = itemId;
        this.userId = userId;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
    }

    public int getBidId() {
        return bidId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public Timestamp getBidTime() {
        return bidTime;
    }
}
