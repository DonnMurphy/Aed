package com.jambons.aed;

import com.google.gson.annotations.SerializedName;

public class Auction {
    private String auctionId;
    @SerializedName("tokenId")
    private String sheepId;

    private String seller;
    private String startingPrice;


    private String endingPrice;

    private String duration;

    private String startedAt;

    private String auctionStatus;

    @SerializedName("winningBidPrice")
    private String finalBidPrice;

    private String auctionWinner;

    public String getAuctionSheepName() {
        return auctionSheepName;
    }

    public void setAuctionSheepName(String auctionSheepName) {
        this.auctionSheepName = auctionSheepName;
    }

    public String getAuctionSheepUid() {
        return auctionSheepUid;
    }

    public void setAuctionSheepUid(String auctionSheepUid) {
        this.auctionSheepUid = auctionSheepUid;
    }

    public String getAuctionSheepImage() {
        return auctionSheepImage;
    }

    public void setAuctionSheepImage(String auctionSheepImage) {
        this.auctionSheepImage = auctionSheepImage;
    }
    @SerializedName("sheep_name")
    private String auctionSheepName;
    @SerializedName("sheep_uid")
    private String auctionSheepUid;
    @SerializedName("sheep_image")
    private String auctionSheepImage;


    public String getAuctionWinner() {
        return auctionWinner;
    }

    public void setAuctionWinner(String auctionWinner) {
        this.auctionWinner = auctionWinner;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getSheepId() {
        return sheepId;
    }

    public void setSheepId(String sheepId) {
        this.sheepId = sheepId;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getEndingPrice() {
        return endingPrice;
    }

    public void setEndingPrice(String endingPrice) {
        this.endingPrice = endingPrice;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(String auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public String getFinalBidPrice() {
        return finalBidPrice;
    }

    public void setFinalBidPrice(String finalBidPrice) {
        this.finalBidPrice = finalBidPrice;
    }

    public Auction(String auctionID, String sheepID, String sellerId, String startPrice, String endPrice, String auctionDuration, String startedAtT, String sheepImage, String sheepName, String sheepUid) {
        //this.ID = uid;
        auctionId = auctionID;
        sheepId = sheepID;
        seller = sellerId;
        startingPrice = startPrice;
        endingPrice = endPrice;
        duration= auctionDuration;
        startedAt = startedAtT;
        auctionSheepImage = sheepImage;
        auctionSheepImage = sheepName;
        auctionSheepUid = sheepUid;
    }
}
