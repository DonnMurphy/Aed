package com.jambons.aed;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Sheep {
    @SerializedName("sheep_id")
    private String sheepId;
    @SerializedName("sheep_name")
    private String sheepName;

    @SerializedName("sheep_dp")
    private String sheepDp;

    @SerializedName("sheep_hp")
    private String sheepHp;

    @SerializedName("sheep_ownerid")
    private String sheepOwner;
    @SerializedName("sheep_imagelink")
    private String sheepImageLink;
    @SerializedName("sheep_dna")
    private String sheepUID;
    @SerializedName("sheep_mint_time")
    private String sheepMintedDate;

    public String getSheepId() {
        return sheepId;
    }

    public void setSheepId(String sheepId) {
        this.sheepId = sheepId;
    }

    public String getSheepName() {
        return sheepName;
    }

    public void setSheepName(String sheepName) {
        this.sheepName = sheepName;
    }

    public String getSheepDp() {
        return sheepDp;
    }

    public void setSheepDp(String sheepDp) {
        this.sheepDp = sheepDp;
    }

    public String getSheepHp() {
        return sheepHp;
    }

    public void setSheepHp(String sheepHp) {
        this.sheepHp = sheepHp;
    }

    public String getSheepOwner() {
        return sheepOwner;
    }

    public void setSheepOwner(String sheepOwner) {
        this.sheepOwner = sheepOwner;
    }

    public String getSheepImageLink() {
        return sheepImageLink;
    }

    public void setSheepImageLink(String sheepImageLink) {
        this.sheepImageLink = sheepImageLink;
    }

    public String getSheepUID() {
        return sheepUID;
    }

    public void setSheepUID(String sheepUID) {
        this.sheepUID = sheepUID;
    }

    public String getSheepMintedDate() {
        return sheepMintedDate;
    }

    public void setSheepMintedDate(String sheepMintedDate) {
        this.sheepMintedDate = sheepMintedDate;
    }

    public Sheep(String id,String name, String damage, String health, String owner, String imageLink, String uid, String mintedDate) {
        sheepId = id;
        sheepName = name;
        sheepDp = damage;
        sheepHp = health;
        sheepOwner = owner;
        sheepUID = uid;
        sheepImageLink = imageLink;
        sheepMintedDate = mintedDate;
    }





    //private static int lastCardId = 0;

    //public static ArrayList<Card> createCardsList(int numCards) {
     //   ArrayList<Card> cards = new ArrayList<Card>();

        //for (int i = 1; i <= numCards; i++) {
          //  cards.add(new Sheep("Sheepie " + ++lastCardId, (i + 1), (i + 3)));
        //}

        //return cards;
   // }
}


