package com.example.lado.banksystem;

import java.text.DateFormat;

/**
 * Created by lado on 29/3/18.
 */

public class CreditInfo {
    private int id;
    private String CardNumber;
    private int CVV;
    private int date;

    public CreditInfo() {

    }
    public CreditInfo(int id, String CardNumber, int CVV, int date) {
        this.id = id;
        this.CardNumber = CardNumber;
        this.CVV = CVV;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public int getCVV() {
        return CVV;
    }

    public int getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
