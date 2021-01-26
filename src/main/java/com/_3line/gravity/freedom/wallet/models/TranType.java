package com._3line.gravity.freedom.wallet.models;

public enum TranType {

    DEBIT("Dr"),CREDIT("Cr") , TRANSFER("Tr");

    private String description;

    TranType(String description) {
        this.description = description ;
    }
}
