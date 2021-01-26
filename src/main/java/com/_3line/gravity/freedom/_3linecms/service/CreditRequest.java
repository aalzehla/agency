package com._3line.gravity.freedom._3linecms.service;

/**
 * Created by FortunatusE on 8/27/2018.
 */
public class CreditRequest {

    private String refNumber;
    private String pan;
    private String amount;
    private String senderInfo;
    private String regNum;
    private String narration;

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(String senderInfo) {
        this.senderInfo = senderInfo;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    @Override
    public String toString() {
        return "CreditRequest{" +
                "refNumber='" + refNumber + '\'' +
                ", pan='" + pan + '\'' +
                ", amount='" + amount + '\'' +
                ", senderInfo='" + senderInfo + '\'' +
                ", regNum='" + regNum + '\'' +
                ", narration='" + narration + '\'' +
                '}';
    }
}
