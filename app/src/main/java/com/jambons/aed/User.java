package com.jambons.aed;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class User implements Serializable {
    @Exclude private String docId;

    private String userId, userName;
    private int accountBalance;


    public User() {

    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public User(String userId, String userName, int accountBal){
        this.userId = userId;
        this.userName = userName;
        this.accountBalance = accountBal;
    }


}
